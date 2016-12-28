from django.contrib import admin

# Register your models here.
from .models import Facultad, Persona, PersonaPlaca, Parqueadero, registroParqueo
admin.site.register(Facultad)
admin.site.register(Persona)
admin.site.register(PersonaPlaca)
admin.site.register(Parqueadero)
admin.site.register(registroParqueo)