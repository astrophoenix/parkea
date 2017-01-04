from django.conf.urls import include, url
from . import views
urlpatterns = [
    url(r'validar_login/(?P<correo>[\w\d@\.-]+)/(?P<clave>[\w\d@\.-]+)/$', views.validar_login),
    url(r'obtener_facultades/$', views.obtener_facultades),
    url(r'obtener_parqueaderosxfacultad/(?P<facultad_id>[0-9]+)/$', views.obtener_parqueaderosxfacultad),
    url(r'obtener_placasxpersona/(?P<persona_id>[0-9]+)/$', views.obtener_placasxpersona),
    url(r'obtener_usuario/(?P<usuario_id>[0-9]+)/$', views.obtener_usuario),
    url(r'registrar_usuario/(?P<nombre>[\w\d@\.-]+)/(?P<apellido>[\w\d@\.-]+)/(?P<placa>[\w\d@\.-]+)/(?P<correo>[\w\d@\.-]+)/(?P<clave>[\w\d@\.-]+)/$', views.registrar_usuario),
    url(r'registrar_parqueo_persona/(?P<parqueadero_id>[0-9]+)/(?P<placa>[\w\d@\.-]+)/$', views.registrar_parqueo_persona),
    url(r'registrar_placa_usuario/(?P<usuario_id>[0-9]+)/(?P<placa>[\w\d@\.-]+)/$', views.registrar_placa_usuario),
]