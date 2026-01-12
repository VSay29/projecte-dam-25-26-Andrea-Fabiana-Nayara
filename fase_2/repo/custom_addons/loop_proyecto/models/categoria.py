# -*- coding: utf-8 -*-

from odoo import models, fields

class categoria(models.Model):
    _name='categoria'
    _description='Modelo de categoría'

    nombre = fields.Char