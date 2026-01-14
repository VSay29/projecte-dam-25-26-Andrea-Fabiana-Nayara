# -*- coding: utf-8 -*-

from odoo import models, fields

class Valoracion(models.Model):

    _name = 'loop_proyecto.valoracion'
    _description = 'Valoracion'

    comentario = fields.Char(
        string='Comentario',
        required=True,
        size=1000,
        store=True
    )
    
    usuario_comentador = fields.Many2one(
        comodel_name='res.users',
        string='Usuario',
        required=True,
        store=True
    )

    usuario_valorado = fields.Many2one(
        comodel_name='res.users',
        string='Usuario',
        required=True,
    )

    valoracion = fields.Integer(
        string='Valoracion',
        required=True,
        default = 0,
        help='Valoracion de 1 a 5 estrellas.'
    )