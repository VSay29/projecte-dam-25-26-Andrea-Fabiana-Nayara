# -*- coding: utf-8 -*-
"""Módulo de compras.

Contiene el modelo `Compra` para el carrito de compra.
"""

from odoo import models, fields


class Compra(models.Model):
    """Modelo de carrito de compra.

    Atributos:
        producto_id (loop_proyecto.producto): Producto comprado.
        comprador_id (res.partner): Comprador.
        vendedor_id (res.partner): Vendedor.
        state (str): Estado de la compra ('disponible', 'reservado', 'vendido').
    """

    _name = 'loop_proyecto.compra'
    _description = 'Carrito de compra'

    producto_id = fields.Many2one(
        'loop_proyecto.producto',
        string="Producto",
        required=True
    )

    comprador_id = fields.Many2one(
        'res.partner',
        string="Comprador",
        required=True
    )

    vendedor_id = fields.Many2one(
        'res.partner',
        string="Vendedor",
        required=True
    )

    state = fields.Selection([
        ('disponible', 'Disponible'),
        ('reservado', 'Reservado'),
        ('vendido', 'Vendido')
    ], default='disponible')