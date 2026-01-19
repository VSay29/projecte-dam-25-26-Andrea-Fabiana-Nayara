# -*- coding: utf-8 -*-
# from odoo import http


# class TokenAuthentication(http.Controller):
#     @http.route('/token_authentication/token_authentication', auth='public')
#     def index(self, **kw):
#         return "Hello, world"

#     @http.route('/token_authentication/token_authentication/objects', auth='public')
#     def list(self, **kw):
#         return http.request.render('token_authentication.listing', {
#             'root': '/token_authentication/token_authentication',
#             'objects': http.request.env['token_authentication.token_authentication'].search([]),
#         })

#     @http.route('/token_authentication/token_authentication/objects/<model("token_authentication.token_authentication"):obj>', auth='public')
#     def object(self, obj, **kw):
#         return http.request.render('token_authentication.object', {
#             'object': obj
#         })

