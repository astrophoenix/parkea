from django.conf.urls import include, url
from . import views
urlpatterns = [
    url(r'validar_login/$', views.validar_login),
    # url(r'cargo/seleccionar/$','contifico.rrhh.views.seleccionar_cargo'),
]