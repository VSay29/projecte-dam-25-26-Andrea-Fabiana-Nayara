# -*- coding: utf-8 -*-
"""Controladores de categorías para la API Loop.

Incluye rutas para listar categorías y otras operaciones relacionadas
con el modelo `loop_proyecto.categoria`.
"""

from odoo import http
from odoo.http import request
from .controladorToken import get_current_user_from_token
import json


class controladorCategoria(http.Controller):
    """Controlador HTTP para operaciones sobre categorías.

    Rutas protegidas mediante token de usuario.
    """

    # --------------------------------------------------------------------------
    #  LISTAR TODAS LAS CATEGORÍAS (GET)
    # --------------------------------------------------------------------------
    @http.route('/api/v1/loop/categorias',
        auth='none',
        methods=['GET'],
        csrf=False,
        type='http'
    )
    def list_categorias(self):
        """Lista todas las categorías disponibles en la aplicación.

        Requiere token válido en el encabezado de autorización.  
        Devuelve JSON con todas las categorías: `id` y `nombre`.

        Returns:
            Response: JSON con `success` y lista de categorías.

        Raises:
            401 Unauthorized: Si el token no es válido o el usuario no existe.
        """
        user = get_current_user_from_token()

        if not user:
            return request.make_response(
                json.dumps({'error': 'Unauthorized'}),
                headers=[('Content-Type', 'application/json')],
                status=401
            )

        categorias = request.env['loop_proyecto.categoria'].sudo().search([])

        result = []

        for categoria in categorias:
            result.append({
                'id': categoria.id,
                'nombre': categoria.nombre,
            })

        return request.make_response(
            json.dumps({'success': True, 'categorias': result}),
            headers=[('Content-Type', 'application/json')],
            status=200
        )