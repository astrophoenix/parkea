from django.db import models

# Create your models here.

class Facultad(models.Model):
	nombre = models.CharField(max_length=30)
	
	def __str__(self):
		return self.nombre

class Parqueadero(models.Model):
	nombre = models.CharField(max_length=30)
	facultad = models.ForeignKey(Facultad, null=True)
	capacidad = models.IntegerField(null=True)
	disponibles = models.IntegerField(null=True)
	
	def __str__(self):
		return self.nombre

class Persona(models.Model):
	ESTUDIANTE = 'E'
	VISITANTE = 'V'
	TIPO_CHOICES = ((ESTUDIANTE, 'Estudiante'), (VISITANTE, 'Visitante'))

	nombre = models.CharField(max_length=30, null=True)
	apellido = models.CharField(max_length=30, null=True)
	correo = models.CharField(max_length=100, null=True)
	clave = models.CharField(max_length=20, null=True)
	facultad = models.ForeignKey(Facultad, null=True)
	tipo = models.CharField(max_length = 2, choices=TIPO_CHOICES, default=VISITANTE)

	def __str__(self):
		return self.nombre

class PersonaPlaca(models.Model):
	persona = models.ForeignKey(Persona)
	placa = models.CharField(max_length=13, null=True)

	def __str__(self):
		return self.placa

class registroParqueo(models.Model):
	facultad = models.ForeignKey(Facultad, null=True)
	parqueadero = models.ForeignKey(Parqueadero, null=True)
	placa = models.CharField(max_length=13, null=True)
	fecha_ingreso = models.DateField(null=True)
	hora_ingreso = models.TimeField(null=True)
	fecha_salida = models.DateField(null=True)
	hora_salida = models.TimeField(null=True)

	def __str__(self):
		return self.parqueadero.nombre + ' | ' + self.placa
	
