# -*- coding: utf-8 -*-
import jwt
import datetime

from odoo import http
from odoo.http import request


def _get_secret_key():
    # Mejor que hardcodear: config param del sistema
    return request.env['ir.config_parameter'].sudo().get_param('jwt.secret_key') or 'una_clave_secreta_segura'


def _get_bearer_token():
    auth_header = request.httprequest.headers.get('Authorization', '')
    if not auth_header.startswith('Bearer '):
        return None
    return auth_header.split(' ', 1)[1].strip()


def get_current_user_from_token():
    token = _get_bearer_token()
    if not token:
        return None

    try:
        payload = jwt.decode(token, _get_secret_key(), algorithms=['HS256'])
        uid = payload.get('uid')
        if not uid:
            return None
        return request.env['res.users'].sudo().browse(int(uid))
    except jwt.ExpiredSignatureError:
        return None
    except jwt.InvalidTokenError:
        return None


class JWTAuthController(http.Controller):

    @http.route('/api/auth', type='json', auth='none', csrf=False, cors='*', methods=['POST'])
    def authenticate(self, **params):
        db = request.session.db
        login = params.get('login')
        password = params.get('password')

        if not db or not login or not password:
            return {'error': 'Missing db/login/password'}

        try:
            uid = request.session.authenticate(db, login, password)
        except Exception:
            return {'error': 'Invalid credentials'}

        if not uid:
            return {'error': 'Authentication failed'}

        payload = {
            'uid': uid,
            'login': login,
            'exp': datetime.datetime.utcnow() + datetime.timedelta(hours=1),
            'iat': datetime.datetime.utcnow(),
        }

        token = jwt.encode(payload, _get_secret_key(), algorithm='HS256')
        if isinstance(token, bytes):
            token = token.decode('utf-8')

        return {'token': token}

    @http.route('/api/user/data', type='json', auth='none', csrf=False, cors='*', methods=['POST'])
    def get_user_data(self, **kw):
        user = get_current_user_from_token()
        if not user or not user.exists():
            return {'error': 'Unauthorized'}

        return {
            'id': user.id,
            'name': user.name,
            'email': user.email,
        }

