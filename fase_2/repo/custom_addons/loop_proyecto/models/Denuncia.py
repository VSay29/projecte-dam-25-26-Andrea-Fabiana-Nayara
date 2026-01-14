# -*- coding: utf-8 -*-

from odoo import models, fields, api

class denunciaReporte(models.Model):
    _name = 'loop_proyecto.denuncia_reporte'
    _description = 'Denuncia de comentarios o productos'
    _order = 'create_date desc'

    denunciante_id = fields.Many2one(
        'res.users',
        string='Usuario denunciante',
        required=True,
        default=lambda self: self.env.user,
        index=True
    )
    
    producto_id = fields.Many2one(
        'marketplace.producto',
        string='Producto denunciado',
        index=True
    )
    
    comentario_id = fields.Many2one(
        'fhm.comentario',
        string='Comentario denunciado',
        index=True
    )
    
    motivoDenuncia = fields.Text(string='Motivo de la denuncia', required=True)
    
    state = fields.Selection(
        [
            ('pendiente', 'Pendiente'),
            ('revisada', 'Revisada'),
            ('cerrada', 'Cerrada'),
        ],
        string='Estado',
        default='pendiente',
        index=True
    )
    
    empleado_id = fields.Many2one(
        'res.users',
        string='Empleado revisor'
    )
    
    fecha_revision = fields.Datetime(string='Fecha de revisión')