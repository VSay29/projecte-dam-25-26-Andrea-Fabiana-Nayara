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
<<<<<<< HEAD
        'views/report_producto.xml',
        'views/vista_producto.xml',
        
=======
        'views/vista_categoria.xml',
       # 'views/vista_producto.xml',
       # 'views/vista_denuncia.xml',
       # 'views/vista_usuario.xml',
        'views/vista_etiquetas.xml',
>>>>>>> 9aab349f87f2caab335bb3b8b0d1ad439c7c08c8
    ],
}
