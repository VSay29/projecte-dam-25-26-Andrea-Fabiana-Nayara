# -*- coding: utf-8 -*-

import json
from odoo import http
from odoo.http import request
from .controladorToken import get_current_user_from_token

class CRUD_User_Controller(http.Controller):
 
    """
    ENDPOINT: REGISTRAR USUARIO
    """

    @http.route('/loop/register', type='json', auth='none', csrf=False, cors='*', methods=['POST'])
    def api_register(self, **params):

        data = params.get('data') # recoge data con los parametros que se le pasan

        # Ejemplo de cómo tiene que ser la estructura a la hora de enviar los datos por JSON:
        #{
        #    "jsonrpc": "2.0",
        #    "method": "call",
        #    "params": {
        #        "data": {
        #            "name": "Juan Jose",
        #            "username": "Juan",
        #            "password": "juan123",
        #            "email": "juan@test.com"
        #        }
        #    }
        #}

        if not data:
            return {'error': 'No se han enviado datos'}
        
        required = ['name','username','password','email'] # datos obligatorios que se controlará más tarde en la app del usuario

        for field in required:
            if field not in data:
                return {'error': f'Falta el campo {field}'}
            

        # Evitar duplicados, en la app también hay que controlar esto, pero se hace también por si acaso en la app se válido el usuario de alguna manera

        if request.env['res.partner'].sudo().search([('username','=',data['username'])], limit=1):
            return {'error': 'El username ya existe'}

        # Se crea el usuario

        try:
            user = request.env['res.partner'].sudo().create({
                'name': data['name'],
                'username': data['username'],
                'password': data['password'],
                'email': data.get('email')
            })
        except Exception as e:
            return {'error': str(e)}
        
        # No hace falta devolver objetos, es sólo un registro
        #return {
        #    'id': user.id,
        #    'name': user.name,
        #    'username': user.username,
        #    'password': user.password,
        #    'email': user.email
        #}

    """
    ENDPOINT: OBTENER USUARIO
    """

    @http.route('/loop/me', type='json', auth="none", cors='*', csrf=False, methods=["GET"])
    def api_get_user(self, **params):
        
        user = get_current_user_from_token()

        if not user:
            return {'error':'Unauthorized'}

        return {
            'id': user.id,
            'name': user.name,
            'username': user.username,
            'email': user.email,
            'phone': user.phone,
            'mobile': user.mobile,
            'idioma': user.idioma
        }

    """
    ENDPOINT: MODIFICAR USUARIO
    """

    @http.route('/loop/me', type='json', auth='none', csrf=False, cors='*', methods=['PUT', 'PATCH'])
    def api_patch_me(self, **params):
        user = get_current_user_from_token()
        if not user:
            return {'error': 'Unauthorized'}

        data = params.get('data')
        if not data:
            return {'error': 'No data to update'}

        allowed = {'name', 'username', 'email', 'phone', 'mobile', 'idioma'}
        update_vals = {
            k: v
            for k, v in data.items()
                if k in allowed
            }

        if not update_vals:
            return {'error': 'No valid fields to update'}

        user.write(update_vals)
        return {'success': True}

    """
    ENDPOINT: BORRAR USUARIO
    """

    @http.route('/loop/me', type='json', auth='none', csrf=False, cors='*', methods=['DELETE'])
    def api_delete_me(self, **params):
        user = get_current_user_from_token()
        if not user:
            return {'error': 'Unauthorized'}

        # borrar el propio usuario

        request.env['res.partner'].with_user(1).browse(user.id).unlink()

        return {'success': True}
