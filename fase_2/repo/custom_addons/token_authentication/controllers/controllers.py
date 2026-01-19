# -*- coding: utf-8 -*-
# import jwt
# import datetime
# from odoo import http
# from odoo.http import request

# SECRET_KEY = 'una_clave_secreta_segura'  # ⚠ Guárdala en variable de entorno en producción
 
# class JWTAuthController(http.Controller):
 
#     @http.route('/api/auth', type='json', auth='none', csrf=False, cors='*',
# methods=['POST'])
#     def authenticate(self, **params):
#         db = http.request.session.db
#         login = params.get('login')
#         password = params.get('password')
 
#         try:
#             uid = request.session.authenticate(db, login, password)
#         except Exception:
#             return {'error': 'Invalid credentials'}
 
#         if uid:
#             payload = {
#                 'uid': uid,
#                 'login': login,
#                 'exp': datetime.datetime.utcnow() + datetime.timedelta(hours=1)
#             }
#             token = jwt.encode(payload, SECRET_KEY, algorithm='HS256')
#             return {'token': token}
 
#         return {'error': 'Authentication failed'}
    
# def get_current_user_from_token():
#     auth_header = request.httprequest.headers.get('Authorization')
#     if not auth_header or not auth_header.startswith('Bearer '):
#         return None
#     token = auth_header.split(' ')[1]
 
#     try:
#         payload = jwt.decode(token, SECRET_KEY, algorithms=['HS256'])
#         uid = payload.get('uid')
#         return http.request.env['res.users'].sudo().browse(uid)
#     except jwt.ExpiredSignatureError:
#         return None
#     except jwt.DecodeError:
#         return None
    
    
# @http.route('/api/user/data', type='json', auth='none', csrf=False, cors='*')
# def get_user_data(self, **kw):
#     user = get_current_user_from_token()
#     if not user:
#         return {'error': 'Unauthorized'}, 401
 
#     return {
#         'id': user.id,
#         'name': user.name,
#         'email': user.email
#     }
