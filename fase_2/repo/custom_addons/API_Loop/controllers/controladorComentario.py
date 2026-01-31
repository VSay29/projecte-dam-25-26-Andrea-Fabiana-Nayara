# -*- coding: utf-8 -*-
from dataclasses import fields
from odoo import http
from odoo.http import request
from pathlib import Path
import json

# CONTROLADOR API REST
# Permite realizar operaciones CRUD sobre comentarios.
# Ejemplos de uso:
# - POST: crear comentario
# - PUT/PATCH: modificar comentario
# - GET: consultar comentario
# - DELETE: eliminar comentario

class controladorComentario(http.Controller):
    
    # --------------------------------------------------------------------------
    #  CREAR COMENTARIO (POST)
    # --------------------------------------------------------------------------
    @http.route('/api/comentarios', auth='bearer', methods=['POST'], csrf=False, type='json')
    def create_comentario(self, **kwargs):
        dicDatos = request.jsonrequest or {}

        # Validaciones mínimas
        producto_id = dicDatos.get("producto_id")
        contenido = dicDatos.get("contenido")

        if not producto_id:
            return {"estado": "Producto no indicado"}
        if not contenido:
            return {"estado": "Contenido no indicado"}

        # Seguridad: el autor debe ser el usuario autenticado por token
        vals = {
            "producto_id": int(producto_id),
            "contenido": contenido,
        }

        # opcional: permitir estado solo si lo decides (si no, quítalo)
        if dicDatos.get("estado") in ("published", "hidden", "deleted"):
            vals["estado"] = dicDatos["estado"]

        record = request.env["loop_proyecto.comentario"].sudo().create(vals)

        # Respuesta limpia (no uses record.read() a pelo si no controlas campos)
        return {
            "estado": "OK",
            "id": record.id,
            "comentario": {
                "id": record.id,
                "contenido": record.contenido,
                "fecha_creacion": record.fecha_creacion,
                "comentador_id": record.comentador_id.id,
                "producto_id": record.producto_id.id,
                "estado": record.estado,
            }
        }

    # --------------------------------------------------------------------------
    #  MODIFICAR COMENTARIO (PUT/PATCH)
    # --------------------------------------------------------------------------
    @http.route('/api/comentarios/<int:comentario_id>', auth='bearer', methods=['PUT', 'PATCH'], csrf=False, type='json')
    def update_comentario(self, comentario_id, **kwargs):
        dicDatos = request.jsonrequest or {}

        comentario = request.env['loop_proyecto.comentario'].sudo().browse(comentario_id)
        if not comentario.exists():
            return {"estado": "Comentario no encontrado"}

        user = request.env.user

        # Seguridad: solo autor o moderador
        es_autor = comentario.comentador_id.id == user.id
        es_moderador = user.has_group('base.group_system')  # ajusta si tienes grupo propio

        if not (es_autor or es_moderador):
            return {"estado": "No autorizado"}

        vals = {}

        # El autor solo puede cambiar el contenido
        if "contenido" in dicDatos and es_autor:
            contenido = dicDatos.get("contenido", "").strip()
            if not contenido:
                return {"estado": "Contenido inválido"}
            vals["contenido"] = contenido

        # El moderador puede cambiar estado
        if "estado" in dicDatos and es_moderador:
            if dicDatos["estado"] not in ("published", "hidden", "deleted"):
                return {"estado": "Estado inválido"}
            vals.update({
                "estado": dicDatos["estado"],
                "moderador_id": user.id,
                "fecha_moderacion": fields.Datetime.now(),
            })

        if not vals:
            return {"estado": "Nada modificable"}

        comentario.write(vals)

        return {
            "estado": "OK",
            "id": comentario.id
        }

    # --------------------------------------------------------------------------
    #  CONSULTAR COMENTARIO (GET)
    # --------------------------------------------------------------------------
    @http.route('/api/comentarios/<int:comentario_id>', auth='bearer', methods=['GET'], csrf=False, type='http')
    def get_comentario(self, comentario_id):
        comentario = request.env['loop_proyecto.comentario'].sudo().browse(comentario_id)
        if not comentario.exists():
            return {"estado": "Comentario no encontrado"}

        user = request.env.user
        es_autor = comentario.comentador_id.id == user.id
        es_moderador = user.has_group('base.group_system')

        # Si no es autor ni moderador, solo puede ver publicados
        if not (es_autor or es_moderador) and comentario.estado != 'published':
            return {"estado": "No autorizado"}

        return {
            "estado": "OK",
            "comentario": {
                "id": comentario.id,
                "contenido": comentario.contenido,
                "estado": comentario.estado,
                "fecha_creacion": comentario.fecha_creacion,
                "autor": {
                    "id": comentario.comentador_id.id,
                    "nombre": comentario.comentador_id.name,
                },
                "producto_id": comentario.producto_id.id,
            }
        }

    # --------------------------------------------------------------------------
    #  ELIMINAR COMENTARIO (DELETE)
    # --------------------------------------------------------------------------
    @http.route('/api/comentarios/<int:comentario_id>', auth='bearer', methods=['DELETE'], csrf=False, type='http')
    def delete_comentario(self, comentario_id):
        comentario = request.env['loop_proyecto.comentario'].sudo().browse(comentario_id)
        if not comentario.exists():
            return {"estado": "Comentario no encontrado"}

        user = request.env.user
        es_autor = comentario.comentador_id.id == user.id
        es_moderador = user.has_group('base.group_system')

        if not (es_autor or es_moderador):
            return {"estado": "No autorizado"}

        vals = {"estado": "deleted"}
        if es_moderador:
            vals.update({"moderador_id": user.id, "fecha_moderacion": fields.Datetime.now()})

        comentario.write(vals)
        return {"estado": "OK", "id": comentario.id, "nuevo_estado": comentario.estado}