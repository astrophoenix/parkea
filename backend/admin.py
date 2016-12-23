from django.contrib import admin

# Register your models here.
from .models import Facultad, Persona, PersonaPlaca
admin.site.register(Facultad)
admin.site.register(Persona)
admin.site.register(PersonaPlaca)