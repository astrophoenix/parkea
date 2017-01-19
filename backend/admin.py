from django.contrib import admin

# Register your models here.
from .models import *
admin.site.register(Facultad)
admin.site.register(Persona)
admin.site.register(PersonaPlaca)
admin.site.register(Parqueadero)
admin.site.register(RegistroParqueo)
admin.site.register(ReportePersona)
admin.site.register(Noticia)