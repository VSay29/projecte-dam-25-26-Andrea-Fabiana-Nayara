#-*- utf-8 -*-

from odoo import models, fields

class UsuariosApp(models.Model):
    _inherit = 'res.partner'
    _description = 'Usuarios de la App'
    _order= 'date_joined desc'

    username = fields.Char(string='Nombre de Usuario', required=True, index=True, help='Nombre único para iniciar sesión en la aplicación.')

    password = fields.Char(string='Password', required=True, hash='sha256', help='Contraseña para la autenticación del usuario.')

    date_joined = fields.Datetime(string='Fecha de Registro', default=fields.Datetime.now, readonly=True, help='Fecha y hora en que el usuario se registró en la aplicación.')

    active = fields.Boolean(string='Activo', default=True, help='Indica si el usuario está activo en la aplicación.')

    rol = fields.Selection([('cliente', 'Cliente'), ('empleado', 'Empleado'), ('admin', 'Administrador')], string='Rol', default='cliente', required=True, help='Rol del usuario en la aplicación.')

    idioma = fields.Selection([('es', 'Español'), ('en', 'Inglés'), ('ca', 'Catalán')], string='Idioma Preferido', default='es', help='Idioma preferido del usuario para la interfaz de la aplicación.')