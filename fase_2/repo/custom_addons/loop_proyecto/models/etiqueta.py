# -*- coding: utf-8 -*-

from odoo import models, fields, api
from odoo.exceptions import ValidationError

class EtiquetaProducto(models.Model):
    _name = 'loop_proyecto.etiqueta_producto'
    _description = 'Etiqueta del producto'
    _order = 'name'

    name = fields.Char(string='Nombre', required=True, index=True)
    active = fields.Boolean(string='Activa', default=True)
    #color = fields.Integer(string='Color')

    _sql_constraints = [
        ('name_uniq', 'unique(name)', 'Ya existe una etiqueta con ese nombre.'),
    ]
 
    @api.constrains('name')
    def _check_name_not_empty(self):
        for rec in self:
            if rec.name and not rec.name.strip():
                raise ValidationError(_('El nombre de la etiqueta no puede estar vacío.'))