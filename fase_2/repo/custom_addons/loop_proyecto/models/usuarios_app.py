# -*- coding: utf-8 -*-

from odoo import models, fields, api

class UsuariosApp(models.Model):

    """Modelo que representa usuarios de la aplicación.

    Extiende `res.partner` añadiendo campos específicos de la App.

    Atributos:
        username (str): Nombre único para iniciar sesión en la aplicación.
        password (str): Contraseña del usuario.
        date_joined (datetime): Fecha de registro del usuario.
        active (bool): Indica si el usuario está activo.
        rol (str): Rol del usuario ('cliente', 'empleado', 'admin').
        idioma (str): Idioma preferido del usuario ('Español', 'Inglés', 'Catalán').
        favorito_ids (loop_proyecto.producto): Productos favoritos del usuario.
        valoracion_ids (loop_proyecto.valoracion): Valoraciones recibidas por el usuario.
        valoracion_media (float): Valoración media calculada.
    """

    _inherit = 'res.partner'
    _description = 'Usuarios de la App'
    _order= 'date_joined desc'

    username = fields.Char(string='Nombre de Usuario', index=True, help='Nombre único para iniciar sesión en la aplicación.')

    password = fields.Char(string='Password', groups="base.group_system")

    date_joined = fields.Datetime(string='Fecha de Registro', default=fields.Datetime.now, readonly=True, help='Fecha y hora en que el usuario se registró en la aplicación.')

    active = fields.Boolean(string='Activo', default=True, help='Indica si el usuario está activo en la aplicación.')

    rol = fields.Selection([('cliente', 'Cliente'), ('empleado', 'Empleado'), ('admin', 'Administrador')], string='Rol', default='cliente', required=True, help='Rol del usuario en la aplicación.')

    idioma = fields.Selection([('es', 'Español'), ('en', 'English'), ('ca', 'Catalán')], string='Idioma Preferido', default='es', help='Idioma preferido del usuario para la interfaz de la aplicación.')

    favorito_ids = fields.Many2many(
        'loop_proyecto.producto',
        'loop_favorito_rel',
        'usuario_id',
        'producto_id',
        string='Favoritos'
    )

    valoracion_ids = fields.One2many(
        'loop_proyecto.valoracion',
        'usuario_valorado',
        string='Valoraciones recibidas'
    )

    valoracion_media = fields.Float(
        string='Valoración media',
        compute='_compute_valoracion_media',
        store=False
    )

    def _compute_valoracion_media(self):
        for record in self:
            if record.valoracion_ids:
                record.valoracion_media = sum(v.valoracion for v in record.valoracion_ids) / len(record.valoracion_ids)
            else:
                record.valoracion_media = 0.0