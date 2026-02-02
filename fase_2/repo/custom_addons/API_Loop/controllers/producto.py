

    #-------------------LISTAR PRODUCTO--------------------
import json
from odoo import http
from odoo.http import request
from .controladorToken import get_current_user_from_token




# ======================================================
# FUNCIONES AUXILIARES
# ======================================================

def unauthorized():
    return request.make_response(
        json.dumps({'error': 'Unauthorized'}),
        headers=[('Content-Type', 'application/json')],
        status=401
    )

class ProductoController(http.Controller):

        
    # ======================================================
    # LISTAR PRODUCTOS
    # ======================================================

    @http.route('/api/products', type='http', auth='none', csrf=False, cors='*', methods=['GET'])
    def get_products(self, **kw):
        user = get_current_user_from_token()
        if not user:
            return unauthorized()

        products = request.env['loop_proyecto.producto'].sudo().search([])

        result = []
        for p in products:
            result.append({
            'id': p.id,
            'nombre': p.nombre,
            'descripcion': p.descripcion,
            'precio': p.precio,
            'estado': p.estado,
            'ubicacion': p.ubicacion,
            'antiguedad': p.antiguedad.isoformat() if p.antiguedad else None,

            'categoria': {
                'id': p.categoria_id.id,
                'nombre': p.categoria_id.nombre,
            },

            'propietario': {
                'id': p.propietario_id.id,
                'nombre': p.propietario_id.name,
            },

            'etiquetas': [
                {'id': e.id, 'nombre': e.name}
                for e in p.etiqueta_ids
            ],

            'imagenes': [
                {
                    'id': img.id,
                    'principal': img.is_principal,
                    'orden': img.sequence,
                }
                for img in p.imagen_ids
            ],
        })


        return request.make_response(
            json.dumps({
                'ok': True,
                'count': len(result),
                'products': result
            }),
            headers=[('Content-Type', 'application/json')],
            status=200
    )

    
    # ======================================================
    # CONSULTAR PRODUCTO POR ID
    # ======================================================
    @http.route('/api/products/<int:product_id>', type='http', auth='none', csrf=False, cors='*', methods=['GET'])
    def get_product(self, product_id, **kw):
        user = get_current_user_from_token()
        if not user:
            return unauthorized()

        product = request.env['loop_proyecto.producto'].sudo().browse(product_id)
        if not product.exists():
            return request.make_response(
                json.dumps({'error': 'Product not found'}),
                headers=[('Content-Type', 'application/json')],
                status=404
            )

        return request.make_response(
            json.dumps({
                'id': product.id,
                'nombre': product.nombre,
                'descripcion': product.descripcion,
                'precio': product.precio,
                'estado': product.estado,
                'ubicacion': product.ubicacion,
                'categoria': {
                    'id': product.categoria_id.id,
                    'nombre': product.categoria_id.nombre,
                }
            }),
            headers=[('Content-Type', 'application/json')],
            status=200
        )


    # ======================================================
    # CREAR PRODUCTO CON IMAGEN
    # ======================================================
    @http.route('/api/products', type='json', auth='none', csrf=False, cors='*', methods=['POST'])
    def create_product(self, **params):
        user = get_current_user_from_token()
        if not user:
            return unauthorized()

        try:
            data = json.loads(request.httprequest.data or '{}')
        except Exception:
            return request.make_response(
                json.dumps({'error': 'Invalid JSON'}),
                headers=[('Content-Type', 'application/json')],
                status=400
            )

        # ---------------- VALIDACIONES ----------------
        required_fields = [
            'nombre', 'descripcion', 'precio', 'estado',
            'ubicacion', 'antiguedad', 'categoria_id', 'imagenes'
        ]

        for field in required_fields:
            if field not in data:
                return request.make_response(
                    json.dumps({'error': f'Missing field: {field}'}),
                    headers=[('Content-Type', 'application/json')],
                    status=400
                )

        if not data['imagenes'] or len(data['imagenes']) < 1:
            return request.make_response(
                json.dumps({'error': 'At least one image is required'}),
                headers=[('Content-Type', 'application/json')],
                status=400
            )

        # ---------------- CREAR PRODUCTO ----------------
        product = request.env['loop_proyecto.producto'].sudo().create({
            'nombre': data['nombre'],
            'descripcion': data['descripcion'],
            'precio': data['precio'],
            'estado': data['estado'],
            'ubicacion': data['ubicacion'],
            'antiguedad': data['antiguedad'],
            'categoria_id': data['categoria_id'],
            'propietario_id': user.id,
        })

        # ---------------- CREAR IMÁGENES ----------------
        for img in data['imagenes']:
            if 'imagen' not in img:
                continue

            request.env['loop_proyecto.producto_imagen'].sudo().create({
                'producto_id': product.id,
                'imagen': img['imagen'],
                'is_principal': img.get('is_principal', False),
                'sequence': img.get('sequence', 10),
            })

        return request.make_response(
            json.dumps({
                'ok': True,
                'product_id': product.id
            }),
            headers=[('Content-Type', 'application/json')],
            status=201
        )

    
     #-------------------ACTUALIZAR PRODUCTO --------------------
    @http.route('/api/products/<int:product_id>', type='http', auth='none', csrf=False, cors='*', methods=['PUT'])
    def update_product(self, product_id, **kw):
        user = get_current_user_from_token()
        if not user:
            return unauthorized()

        product = request.env['loop_proyecto.producto'].sudo().browse(product_id)
        if not product.exists():
            return request.make_response(
                json.dumps({'error': 'Product not found'}),
                headers=[('Content-Type', 'application/json')],
                status=404
            )

        if product.propietario_id.id != user.id:
            return request.make_response(
                json.dumps({'error': 'Forbidden'}),
                headers=[('Content-Type', 'application/json')],
                status=403
            )

        data = json.loads(request.httprequest.data or '{}')

        allowed_fields = ['nombre', 'descripcion', 'precio', 'estado', 'ubicacion', 'categoria_id', 'antiguedad']
        vals = {k: v for k, v in data.items() if k in allowed_fields}

        if vals:
            product.write(vals)

        return request.make_response(
            json.dumps({'ok': True}),
            headers=[('Content-Type', 'application/json')],
            status=200
        )


     #-------------------ELIMINAR PRODUCTO --------------------
    @http.route('/api/products/<int:product_id>', type='http', auth='none', csrf=False, cors='*', methods=['DELETE'])
    def delete_product(self, product_id, **kw):
        user = get_current_user_from_token()
        if not user:
            return unauthorized()

        product = request.env['loop_proyecto.producto'].sudo().browse(product_id)
        if not product.exists():
            return request.make_response(
                json.dumps({'error': 'Product not found'}),
                headers=[('Content-Type', 'application/json')],
                status=404
            )

        if product.propietario_id.id != user.id:
            return request.make_response(
                json.dumps({'error': 'Forbidden'}),
                headers=[('Content-Type', 'application/json')],
                status=403
            )

        product.unlink()  # o soft delete si prefieres

        return request.make_response(
            json.dumps({'ok': True}),
            headers=[('Content-Type', 'application/json')],
            status=200
        )





