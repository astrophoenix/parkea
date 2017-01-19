from django.conf.urls import include, url
from . import views
urlpatterns = [
    url(r'validar_login/(?P<correo>[\w\d@\.-]+)/(?P<clave>[\w\d@\.-]+)/$', views.validar_login),
    url(r'obtener_facultades/$', views.obtener_facultades),
    url(r'obtener_parqueaderosxfacultad/(?P<facultad_id>[0-9]+)/$', views.obtener_parqueaderosxfacultad),
    url(r'obtener_placasxpersona/(?P<persona_id>[0-9]+)/$', views.obtener_placasxpersona),
    url(r'obtener_usuario/(?P<usuario_id>[0-9]+)/$', views.obtener_usuario),
    url(r'registrar_usuario/(?P<nombre>[\w\d@\.-]+)/(?P<apellido>[\w\d@\.-]+)/(?P<placa>[\w\d@\.-]+)/(?P<correo>[\w\d@\.-]+)/(?P<clave>[\w\d@\.-]+)/$', views.registrar_usuario),
    url(r'registrar_parqueo_persona/(?P<parqueadero_id>[0-9]+)/(?P<placa>[\w\d@\.-]+)/(?P<longitud>.+)/(?P<latitud>.+)/(?P<usuario_id>[0-9]+)/$', views.registrar_parqueo_persona),
    url(r'registrar_placa_usuario/(?P<usuario_id>[0-9]+)/(?P<placa>[\w\d@\.-]+)/$', views.registrar_placa_usuario),
    url(r'obtener_historial_parqueo_persona/(?P<usuario_id>[0-9]+)/$', views.obtener_historial_parqueo_persona),
    url(r'actualizar_estado_parqueo_persona/(?P<parqueo_id>[0-9]+)/$', views.actualizar_estado_parqueo_persona),
    url(r'registrar_reporte_parqueos/(?P<parqueadero_id>[0-9]+)/(?P<num_parqueos_ocupados>[0-9]+)/(?P<usuario_id>[0-9]+)/$', views.registrar_reporte_parqueos),
    url(r'obtener_eventos/$', views.obtener_eventos),
    url(r'obtener_recompensaxusuario/(?P<usuario_id>[0-9]+)/$', views.obtener_recompensaxusuario),
    url(r'obtener_rating_usuario/(?P<usuario_id>[0-9]+)/$', views.obtener_rating_usuario),

]