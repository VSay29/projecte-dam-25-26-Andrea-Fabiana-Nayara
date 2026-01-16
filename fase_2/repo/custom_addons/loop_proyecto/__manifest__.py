# -*- coding: utf-8 -*-
{
    'name': "Loop",
    'summary': "App para compra-venta de productos",
    'description': """
        Con Loop puedes comprar y vender productos de manera fácil y rápida.
        Plataforma de compra-venta para el proyecto DAM.
    """,

    'author': "Andrea, Fabiana y Nayara",
    'website': "https://www.yourcompany.com",

    # CATEGORÍA VÁLIDA
    'category': 'Productivity',
    'version': '1.0',

    # MUY IMPORTANTE
    'installable': True,
    'application': True,

    'depends': ['base'],

    'data': [
        'security/ir.model.access.csv',
        'views/report_producto.xml',
        'views/vista_categoria.xml',
         'views/vista_producto.xml',
       # 'views/vista_denuncia.xml',
       # 'views/vista_usuario.xml',
        'views/vista_etiquetas.xml',
    ],
}
