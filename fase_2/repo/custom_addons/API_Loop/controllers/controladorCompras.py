# -*- coding: utf-8 -*-
"""Controlador API REST para operaciones de compras y ventas en Loop Proyecto.

Permite a los usuarios realizar compras, consultar su carrito y ventas en proceso,
y actualizar el estado de una compra.

Autor: Andrea, Fabiana y Nayara
"""

import json
import logging
from odoo import http
from odoo.http import request
from .controladorToken import get_current_user_from_token


class CRUD_Compra_Controller(http.Controller):
    """Controlador HTTP para la gestión de compras y ventas."""

    @http.route(
        '/api/v1/loop/compra/comprar',
        type='json',
        auth='none',
        csrf=False,
        cors='*',
        methods=['POST']
    )
    def api_realizar(self, **params):
        """Crea una nueva compra de un producto.

        Args:
            params: Diccionario que debe contener:
                data: {
                    'producto_id': ID del producto,
                    'vendedor_id': ID del vendedor,
                    'comprador_id': ID del comprador,
                    'state': Estado inicial de la compra
                }

        Returns:
            dict: {'success': True, 'compra_id': <ID>} si se creó correctamente,
                  {'error': <mensaje>} en caso de fallo.

        Raises:
            ValueError: Si faltan datos obligatorios o hay error al crear la compra.
        """
        data = params.get('data')

        if not data:
            return {'error': 'No se han enviado datos'}

        required = ['producto_id', 'vendedor_id', 'comprador_id']
        for field in required:
            if field not in data:
                return {'error': f'Falta indicar el campo {field}'}

        try:
            compra = request.env['loop_proyecto.compra'].sudo().create({
                'producto_id': data['producto_id'],
                'vendedor_id': data['vendedor_id'],
                'comprador_id': data['comprador_id'],
                'state': data['state']
            })
            return {'success': True, 'compra_id': compra.id}
        except Exception as ex:
            return {'error': str(ex)}

    @http.route(
        '/api/v1/loop/compras/obtener',
        type='json',
        auth='none',
        cors='*',
        csrf=False,
        methods=['GET']
    )
    def api_get_compras(self, **params):
        """Obtiene todas las compras de un usuario (carrito).

        Requiere token válido.

        Args:
            params: Diccionario de parámetros (no se requieren campos específicos).

        Returns:
            list: Lista de compras con campos:
                - id: ID de la compra
                - producto_id: ID del producto
                - state: Estado de la compra

        Raises:
            401 Unauthorized: Si el token no es válido.
        """
        user = get_current_user_from_token()
        if not user:
            return {'error': 'Unauthorized'}

        compras = request.env['loop_proyecto.compra'].sudo().search([('comprador_id', '=', user.id)])
        resultado = [{'id': c.id, 'producto_id': c.producto_id.id, 'state': c.state} for c in compras]
        return resultado

    @http.route(
        '/api/v1/loop/ventas/obtener',
        type='json',
        auth='none',
        cors='*',
        csrf=False,
        methods=['GET']
    )
    def api_get_ventas(self, **params):
        """Obtiene todas las ventas en proceso de un usuario.

        Requiere token válido.

        Args:
            params: Diccionario de parámetros (no se requieren campos específicos).

        Returns:
            list: Lista de ventas con campos:
                - id: ID de la venta
                - producto_id: ID del producto
                - state: Estado de la venta

        Raises:
            401 Unauthorized: Si el token no es válido.
        """
        user = get_current_user_from_token()
        if not user:
            return {'error': 'Unauthorized'}

        ventas = request.env['loop_proyecto.compra'].sudo().search([('vendedor_id', '=', user.id)])
        resultado = [{'id': v.id, 'producto_id': v.producto_id.id, 'state': v.state} for v in ventas]
        return resultado

    @http.route(
        '/api/v1/loop/cambiarEstado',
        type='json',
        auth='none',
        cors=False,
        methods=['PATCH']
    )
    def api_updateState(self, **params):
        """Modifica el estado de una compra (por ejemplo, marcar como vendida).

        Requiere token válido.

        Args:
            params: Diccionario que debe contener:
                - compra_id: ID de la compra a modificar
                - state: Nuevo estado de la compra

        Returns:
            dict: {'ok': True} si se actualizó correctamente, {'error': <mensaje>} en caso de fallo.

        Raises:
            401 Unauthorized: Si el token no es válido.
            ValueError: Si faltan parámetros o la compra no existe.
        """
        user = get_current_user_from_token()
        if not user:
            return {'error': 'Unauthorized'}

        compra_id = params.get('compra_id')
        state = params.get('state')

        if not compra_id or not state:
            return {'error': 'Faltan parámetros'}

        compra = request.env['loop_proyecto.compra'].sudo().browse(compra_id)
        if not compra.exists():
            return {'error': 'Compra no encontrada'}

        compra.state = state
        return {'ok': True}