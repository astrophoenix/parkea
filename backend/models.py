from django.db import models

# Create your models here.

class Facultad(models.Model):
	nombre = models.CharField(max_length=30)
	
	def __str__(self):
		return self.nombre

class Persona(models.Model):
	ESTUDIANTE = 'E'
	VISITANTE = 'V'
	TIPO_CHOICES = ((ESTUDIANTE, 'Estudiante'), (VISITANTE, 'Visitante'))

	nombre = models.CharField(max_length=30)
	apellido = models.CharField(max_length=30)
	correo = models.CharField(max_length=100)
	facultad = models.ForeignKey(Facultad, null=True)
	tipo = models.CharField(max_length = 2, choices=TIPO_CHOICES, default=VISITANTE)

	def __str__(self):
		return self.nombre

class PersonaPlaca(models.Model):
	persona = models.ForeignKey(Persona)
	placa = models.CharField(max_length=13, null=True)