# -*- coding: utf-8 -*-
from odoo import models, fields, api
from odoo.exceptions import ValidationError


class Producto(models.Model):
    _name = 'marketplace.producto'
    _description = 'Producto del Marketplace'

    # =========================
    # CAMPOS OBLIGATORIOS
    # =========================

    propietario_id = fields.Many2one(
        'res.users',
        string='Propietario',
        required=True,
        default=lambda self: self.env.user
    )

    nombre = fields.Char(
        string='Nombre del producto',
        required=True
    )

    descripcion = fields.Text(
        string='Descripción',
        required=True
    )

    antiguedad = fields.Date(
        string='Antigüedad del producto',
        required=True
    )

    estado = fields.Selection(
        [
            ('nuevo', 'Nuevo'),
            ('segunda_mano', 'Segunda mano'),
            ('reacondicionado', 'Reacondicionado')
        ],
        string='Estado del producto',
        required=True
    )

    precio = fields.Float(
        string='Precio (€)',
        required=True
    )

    ubicacion = fields.Char(
        string='Ubicación',
        required=True
    )

    # =========================
    # ETIQUETAS (MÁXIMO 5)
    # =========================

    etiqueta_ids = fields.Many2many(
        'marketplace.etiqueta',
        string='Etiquetas'
    )

    # =========================
    # IMÁGENES (1 A 10)
    # =========================

    imagen_ids = fields.One2many(
        'marketplace.producto.imagen',
        'producto_id',
        string='Imágenes'
    )

    # =========================
    # VALIDACIONES
    # =========================

    @api.constrains('etiqueta_ids')
    def _check_max_etiquetas(self):
        for record in self:
            if len(record.etiqueta_ids) > 5:
                raise ValidationError(
                    'Un producto no puede tener más de 5 etiquetas.'
                )

    @api.constrains('imagen_ids')
    def _check_numero_imagenes(self):
        for record in self:
            if len(record.imagen_ids) < 1:
                raise ValidationError(
                    'Debe añadirse al menos una imagen al producto.'
                )
            if len(record.imagen_ids) > 10:
                raise ValidationError(
                    'No se pueden añadir más de 10 imágenes al producto.'
                )