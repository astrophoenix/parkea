# coding=utf-8
from django.db import models
import requests 
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

	@classmethod
	def actualizarDisponibilidadParqueadero(cls, parqueo_id):
		#Se actualiza la disponibilidad del parqueadero
		parqueadero = Parqueadero.objects.get(pk=parqueo_id)
		parqueos_ocupados = RegistroParqueo.objects.filter(estado='A')
		num_parqueos_ocupados = parqueos_ocupados.count()
		disponibles = parqueadero.capacidad - num_parqueos_ocupados
		parqueadero.disponibles = disponibles
		parqueadero.save()
	
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

	@classmethod
	def validar_placa(cls, placa):
		# GHR0263
		placa = placa.replace("-", "")
		placa = placa.upper()
		url = "http://sistemaunico.ant.gob.ec:6033/PortalWEB/paginas/clientes/clp_json_consulta_persona.jsp?ps_tipo_identificacion=PLA&ps_identificacion="+str(placa)
		data = requests.get(url).json()
		return data['mensaje']

	def __str__(self):
		return self.placa

class RegistroParqueo(models.Model):
	usuario = models.ForeignKey(Persona, null=True)
	facultad = models.ForeignKey(Facultad, null=True)
	parqueadero = models.ForeignKey(Parqueadero, null=True)
	placa = models.CharField(max_length=13, null=True)
	fecha_ingreso = models.DateField(null=True)
	hora_ingreso = models.TimeField(null=True)
	fecha_salida = models.DateField(null=True)
	hora_salida = models.TimeField(null=True)
	longitud = models.DecimalField(null=True, max_digits=13, decimal_places=10)
	latitud = models.DecimalField(null=True, max_digits=13, decimal_places=10)
	estado = models.CharField(max_length=2, null=True)

	def __str__(self):
		return self.parqueadero.nombre + ' | ' + self.placa + '|' + self.estado
	
class ReportePersona(models.Model):
	parqueadero = models.ForeignKey(Parqueadero, null=True)
	num_parqueos_ocupados = models.IntegerField(null=True)
	persona = models.ForeignKey(Persona, null=True)
	fecha_creacion = models.DateTimeField(auto_now_add=True)
	ultima_modificacion = models.DateTimeField(auto_now=True)

class Noticia(models.Model):
	TIPO_NOTICIA_EVENTO = 'E'
	TIPO_NOTICIA_RECOMPENSA = 'R'
	descripcion = models.CharField(max_length=200, null=True)
	tipo = models.CharField(max_length=2, null=True)
	usuario = models.ForeignKey(Persona, null=True)
	fecha_creacion = models.DateTimeField(auto_now_add=True)
	ultima_modificacion = models.DateTimeField(auto_now=True)

	def __str__(self):
		return self.descripcion + ' | ' + self.tipo