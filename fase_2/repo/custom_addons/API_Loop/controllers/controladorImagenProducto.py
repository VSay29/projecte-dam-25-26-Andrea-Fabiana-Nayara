# -*- coding: utf-8 -*-
import json
from odoo import http
from odoo.http import request
from .controladorToken import get_current_user_from_token


class ControladorImagenProducto(http.Controller):
    """Controlador HTTP para gestionar las imágenes de productos en Loop Proyecto."""

    @http.route(
        '/api/v1/loop/productos/<int:product_id>/imagenes',
        auth='none',
        methods=['GET'],
        csrf=False,
        type='http'
    )
    def get_imagenes_producto(self, product_id):
        """
        Obtiene todas las imágenes de un producto dado su ID.

        Args:
            product_id (int): ID del producto del que se quieren obtener las imágenes.

        Returns:
            Response HTTP con JSON:
                - En caso de éxito (200):
                    {
                        'ok': True,
                        'imagenes': [
                            {
                                'id': <id_imagen>,
                                'imagen': <base64>,
                                'is_principal': <bool>,
                                'sequence': <int>
                            },
                            ...
                        ]
                    }
                - En caso de error:
                    - 401 Unauthorized: {'error': 'Unauthorized'}
                    - 404 Not Found: {'error': 'Producto no encontrado'}
        """
        user = get_current_user_from_token()
        if not user:
            return request.make_response(
                json.dumps({'error': 'Unauthorized'}),
                headers=[('Content-Type', 'application/json')],
                status=401
            )

        product = request.env['loop_proyecto.producto'].sudo().browse(product_id)
        if not product.exists():
            return request.make_response(
                json.dumps({'error': 'Producto no encontrado'}),
                headers=[('Content-Type', 'application/json')],
                status=404
            )

        imagenes = []
        for img in product.imagen_ids:
            imagenes.append({
                'id': img.id,
                'imagen': img.imagen.decode('utf-8') if img.imagen else None,
                'is_principal': img.is_principal,
                'sequence': img.sequence,
            })

        return request.make_response(
            json.dumps({'ok': True, 'imagenes': imagenes}),
            headers=[('Content-Type', 'application/json')],
            status=200
        )