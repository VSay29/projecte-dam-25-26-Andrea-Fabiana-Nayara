# -*- coding: utf-8 -*-
{
    'name': "Loop",

    'summary': "App para compra-venta de productos",

    'description': """
        Con Loop puedes comprar y vender productos de manera fácil y rápida.
        Loop ofrece una plataforma intuitiva para que los usuarios publiquen sus productos y encuentren ofertas atractivas.
        Loop facilita la comunicación entre compradores y vendedores, asegurando transacciones seguras y satisfactorias.
        Loop es la solución ideal para quienes buscan una experiencia de compra-venta confiable y eficiente.
        Loop: tu mercado en línea de confianza.
        Loopea tus productos con un click.
    """,

    'author': "Andrea-Fabian-Nayara",
    'website': "https://www.yourcompany.com",

    # Categories can be used to filter modules in modules listing
    # Check https://github.com/odoo/odoo/blob/15.0/odoo/addons/base/data/ir_module_category_data.xml
    # for the full list
    'category': 'Uncategorized',
    'version': '0.1',

    'application': True,

    # any module necessary for this one to work correctly
    'depends': ['base'],

    # always loaded
    'data': [
        'security/ir.model.access.csv',
    ],
}

