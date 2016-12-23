from django.conf.urls import include, url
from django.contrib import admin

urlpatterns = [
    # Examples:
    # url(r'^$', 'parkea.views.home', name='home'),
    url(r'^backend/', include('backend.urls')),
    url(r'^admin/', include(admin.site.urls)),
]
