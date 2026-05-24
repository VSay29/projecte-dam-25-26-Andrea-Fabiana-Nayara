# -*- coding: utf-8 -*-

import json
import base64
from venv import logger
from odoo import http
from odoo.http import request
from .controladorToken import get_current_user_from_token
from pathlib import Path
from passlib.context import CryptContext

def img_a_base64(ruta):
    """ Convierte una imagen de la ruta proporcionada a base64 """
    try:
        if not Path(ruta).exists():
            raise FileNotFoundError(f"La ruta {ruta} no es válida.")
        
        with open(ruta, "rb") as image_file:
            return base64.b64encode(image_file.read()).decode('utf-8')
        
    except Exception as e:
        return None

class CRUD_User_Controller(http.Controller):
 
    """
    ENDPOINT: REGISTRAR USUARIO
    """

    @http.route('/api/v1/loop/register', type='json', auth='none', csrf=False, cors='*', methods=['POST'])
    def api_register(self, **params):

        pwd_context = CryptContext(schemes=["bcrypt"], deprecated="auto")

        data = params.get('data') # recoge data con los parametros que se le pasan

        if not data:
            return {'error': 'No se han enviado datos'}
        
        required = ['name','username','password','email'] # datos obligatorios que se controlará más tarde en la app del usuario

        for field in required:
            if field not in data:
                return {'error': f'Falta el campo {field}'}
            

        # Evitar duplicados, en la app también hay que controlar esto, pero se hace también por si acaso en la app se válido el usuario de alguna manera

        if request.env['res.partner'].sudo().search([('username','=',data['username'])], limit=1):
            return {'error': 'El username ya existe'}
        
        passwd_plano = data['password']
        passwd_encriptado = pwd_context.hash(passwd_plano)

        # Se crea el usuario

        try:
            user = request.env['res.partner'].sudo().create({
                'name': data['name'], 
                'username': data['username'],
                'password': passwd_encriptado,
                'email': data.get('email')
            })

            try:
                template = request.env['mail.template'].sudo().browse(7)
                if template:
                    template.send_mail(user.id, force_send=True)
            except Exception as mail_e:
                logger.error(f"Error enviando mail: {str(mail_e)}")

            return {'success': True}
        except Exception as e:
            return {'error': str(e)}

    """
    ENDPOINT: OBTENER USUARIO
    """

    @http.route('/api/v1/loop/me', type='json', auth="none", cors='*', csrf=False, methods=["GET"])
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
            'idioma': user.idioma,
            'image_1920': user.image_1920.decode('utf-8') if user.image_1920 else ''
        }

    """
    ENDPOINT: MODIFICAR USUARIO
    """

    @http.route('/api/v1/loop/me', type='json', auth='none', csrf=False, cors='*', methods=['PATCH'])
    def api_patch_me(self, **params):
        user = get_current_user_from_token()
        if not user:
            return {'error': 'Unauthorized'}

        data = params.get('data')
        if not data:
            return {'error': 'No data to update'}

        allowed = {'name', 'username', 'email', 'phone', 'mobile', 'idioma', 'image_1920', 'password'}
        update_vals = {k: v for k, v in data.items() if k in allowed}

        if 'password' in update_vals:
            pwd_context = CryptContext(schemes=["bcrypt"], deprecated="auto")
            update_vals['password'] = pwd_context.hash(update_vals['password'])

        # Validar que image_1920 sea base64 si viene
        if 'image_1920' in update_vals:
            valor = update_vals['image_1920']
            try:
                # quitar espacios y saltos de línea
                valor = valor.replace('\n', '').replace('\r', '')
                base64.b64decode(valor)
            except Exception:
                return {'error': 'La imagen no está en formato base64 válido'}

        if not update_vals:
            return {'error': 'No valid fields to update'}

        request.env['res.partner'].with_user(1).browse(user.id).write(update_vals)
        return {'success': True}


    """
    ENDPOINT: OBTENER PERFIL PÚBLICO DE UN USUARIO POR ID
    """

    @http.route('/api/v1/loop/users/<int:partner_id>', type='http', auth='none', csrf=False, cors='*', methods=['GET'])
    def get_user_by_id(self, partner_id, **kw):
        user = get_current_user_from_token()
        if not user:
            return request.make_response(
                json.dumps({'error': 'Unauthorized'}),
                headers=[('Content-Type', 'application/json')],
                status=401
            )

        partner = request.env['res.partner'].sudo().browse(partner_id)
        if not partner.exists():
            return request.make_response(
                json.dumps({'error': 'User not found'}),
                headers=[('Content-Type', 'application/json')],
                status=404
            )

        return request.make_response(
            json.dumps({
                'id': partner.id,
                'nombre': partner.name,
                'imagen': partner.image_1920.decode('utf-8') if partner.image_1920 else ''
            }),
            headers=[('Content-Type', 'application/json')],
            status=200
        )

    """
    ENDPOINT: BORRAR USUARIO
    """

    @http.route('/api/v1/loop/me', type='json', auth='none', csrf=False, cors='*', methods=['DELETE'])
    def api_delete_me(self, **params):
        user = get_current_user_from_token()
        if not user:
            return {'error': 'Unauthorized'}

        # Borrar el propio usuario

        request.env['res.partner'].with_user(1).browse(user.id).unlink()

        return {'success': True}


    """
    ENDPOINT: OBTENER FAVORITOS
    """

    @http.route('/api/v1/loop/favoritos', type='json', auth='none', csrf=False, cors='*', methods=['GET'])
    def get_favoritos(self, **params):
        user = get_current_user_from_token()
        if not user:
            return {'error': 'Unauthorized'}

        productos = []
        for p in user.favorito_ids:
            imagenes = [img.imagen for img in p.imagen_ids]
            productos.append({
                'id': p.id,
                'nombre': p.nombre,
                'descripcion': p.descripcion,
                'precio': p.precio,
                'ubicacion': p.ubicacion,
                'imagenes': imagenes
            })

        return {'result': productos}
    

    """
    ENDPOINT: AGREGAR FAVORITO
    """

    @http.route('/api/v1/loop/favoritos/add', type='json', auth='none', csrf=False, cors='*', methods=['POST'])
    def add_favorito(self, **params):
        user = get_current_user_from_token()
        if not user:
            return {'error': 'Unauthorized'}

        data = params.get('data')
        if not data or 'product_id' not in data:
            return {'error': 'product_id requerido'}

        try:
            producto_id = int(data.get('product_id'))
        except (TypeError, ValueError):
            return {'error': 'product_id inválido'}

        producto = request.env['loop_proyecto.producto'].sudo().browse(producto_id)
        if not producto.exists():
            return {'error': 'Producto no encontrado'}

        if producto.id in user.favorito_ids.ids:
            return {'error': 'Ya está en favoritos'}

        user.write({'favorito_ids': [(4, producto.id)]})
        return {'success': True}
    
    """
    ENDPOINT: ELIMINAR FAVORITO
    """
    
    @http.route('/api/v1/loop/favoritos/remove', type='json', auth='none', csrf=False, cors='*', methods=['POST'])
    def remove_favorito(self, **params):
        user = get_current_user_from_token()
        if not user:
            return {'error': 'Unauthorized'}

        data = params.get('data')
        if not data or 'producto_id' not in data:
            return {'error': 'producto_id requerido'}

        try:
            producto_id = int(data.get('producto_id'))
        except (TypeError, ValueError):
            return {'error': 'producto_id inválido'}

        if producto_id not in user.favorito_ids.ids:
            return {'error': 'No está en favoritos'}

        user.sudo().write({'favorito_ids': [(3, producto_id)]})
        return {'success': True}


    """
    ENDPOINT: OBTENER CARRITO
    """

    @http.route('/api/v1/loop/carrito', type='json', auth='none', csrf=False, cors='*', methods=['GET'])
    def get_carrito(self, **params):
        user = get_current_user_from_token()
        if not user:
            return {'error': 'Unauthorized'}

        productos = []
        for p in user.carrito_ids:
            imagenes = [img.imagen for img in p.imagen_ids]
            productos.append({
                'id': p.id,
                'nombre': p.nombre,
                'descripcion': p.descripcion,
                'precio': p.precio,
                'ubicacion': p.ubicacion,
                'imagenes': imagenes
            })

        return {'productos': productos}


    """
    ENDPOINT: AÑADIR PRODUCTO AL CARRITO
    """

    @http.route('/api/v1/loop/carrito/add', type='json', auth='none', csrf=False, cors='*', methods=['POST'])
    def add_carrito(self, **params):
        user = get_current_user_from_token()
        if not user:
            return {'error': 'Unauthorized'}

        data = params.get('data')
        if not data or 'producto_id' not in data:
            return {'error': 'producto_id requerido'}

        try:
            producto_id = int(data.get('producto_id'))
        except (TypeError, ValueError):
            return {'error': 'producto_id inválido'}

        producto = request.env['loop_proyecto.producto'].sudo().browse(producto_id)
        if not producto.exists():
            return {'error': 'Producto no encontrado'}

        if producto_id in user.carrito_ids.ids:
            return {'error': 'Ya está en el carrito'}

        user.write({'carrito_ids': [(4, producto_id)]})
        return {'success': True}


    """
    ENDPOINT: ELIMINAR PRODUCTO DEL CARRITO
    """

    @http.route('/api/v1/loop/carrito/remove', type='json', auth='none', csrf=False, cors='*', methods=['POST'])
    def remove_carrito(self, **params):
        user = get_current_user_from_token()
        if not user:
            return {'error': 'Unauthorized'}

        data = params.get('data')
        if not data or 'producto_id' not in data:
            return {'error': 'producto_id requerido'}

        try:
            producto_id = int(data.get('producto_id'))
        except (TypeError, ValueError):
            return {'error': 'producto_id inválido'}

        if producto_id not in user.carrito_ids.ids:
            return {'error': 'No está en el carrito'}

        user.sudo().write({'carrito_ids': [(3, producto_id)]})
        return {'success': True}