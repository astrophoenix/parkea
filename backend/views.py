# coding=utf-8
from django.shortcuts import render
from models import *
from django.core import serializers
from django.http import HttpResponseForbidden, HttpResponseRedirect, HttpResponse
import json
from urllib import unquote
from django.db import transaction
from datetime import datetime
from django.core.serializers.json import DjangoJSONEncoder
from decimal import *
import math

def validar_login(request, correo, clave):
	personas = Persona.objects.filter(correo=unquote(correo), clave=unquote(clave))
	if personas.count() == 1:
		persona = personas.first()
		estado = persona.id
	else:
		estado = 0
	data = {'estado': estado}
	return HttpResponse(json.dumps(data), content_type='application/json')


def obtener_usuario(request, usuario_id):
	usuario = Persona.objects.get(pk=usuario_id)
	data = {'nombre': usuario.nombre, 'apellido': usuario.apellido, 'correo':usuario.correo}
	return HttpResponse(json.dumps(data), content_type='application/json')

def registrar_usuario(request, nombre, apellido, placa, correo, clave):
	personas = Persona.objects.filter(correo=unquote(correo))
	# No existen Personas previas con el usuario.
	if personas.count() == 0:
		#Debe registrar el nuevo usuario visitante
		try:
			persona = Persona()
			persona.nombre = nombre
			persona.apellido = apellido
			persona.correo = correo
			persona.clave = clave
			persona.tipo = Persona.VISITANTE
			persona.save()
			detalle_placa = PersonaPlaca(persona=persona, placa=placa)
			detalle_placa.save()
			estado = persona.id
		except Exception as e:
			estado = 0	
	else:
		# Existen Personas previas con el usuario.
		estado = 0
	data = {'estado': estado}
	return HttpResponse(json.dumps(data), content_type='application/json')

def registrar_parqueo_persona(request, parqueadero_id, placa, longitud, latitud, usuario_id):
	estado = 0
	if parqueadero_id and placa:
		try:
			hoy = datetime.now()
			parqueadero = Parqueadero.objects.get(pk=parqueadero_id)		
			reg_parqueo = RegistroParqueo()
			reg_parqueo.usuario_id = usuario_id
			reg_parqueo.facultad = parqueadero.facultad
			reg_parqueo.parqueadero = parqueadero
			reg_parqueo.placa = placa
			reg_parqueo.fecha_ingreso = hoy
			reg_parqueo.hora_ingreso = hoy.time()
			reg_parqueo.longitud = longitud
			reg_parqueo.latitud = latitud
			reg_parqueo.estado = 'A'
			reg_parqueo.save()
			Parqueadero.actualizarDisponibilidadParqueadero(parqueadero_id)
			estado = 1
		except Exception as e:
			pass
	data = {'estado': estado}
	return HttpResponse(json.dumps(data), content_type='application/json')

def obtener_facultades(request):
	facultades = Facultad.objects.values_list('id', 'nombre')
	arr_facultades = []
	for facultad in facultades:
		parqueaderos = Parqueadero.objects.filter(facultad__id=facultad[0])
		num_parqueaderos = parqueaderos.count()
		facultad_item = { "id": facultad[0], "nombre": facultad[1], 'num_parqueos': num_parqueaderos }
		arr_facultades.append(facultad_item)
	data = json.dumps(arr_facultades)
	return HttpResponse(data, content_type='application/json')


def obtener_historial_parqueo_persona(request, usuario_id):
	data = {}
	parqueos_persona = RegistroParqueo.objects.filter(usuario__id=int(usuario_id)).order_by('-id').values_list('id', 'usuario', 'facultad', 'parqueadero', 'placa', 'fecha_ingreso', 'hora_ingreso', 'fecha_salida', 'hora_salida', 'longitud', 'latitud', 'estado')
	data = json.dumps([{"id": p[0], "persona_id": p[1], "facultad_id": p[2], "parqueadero_id":p[3], "placa":p[4], "fecha_ingreso":p[5], "hora_ingreso":p[6], "fecha_salida":p[7], "hora_salida":p[8], "longitud":p[9], "latitud":p[10], "estado":p[11] } for p in parqueos_persona], cls=DjangoJSONEncoder)
	return HttpResponse(data, content_type='application/json')


def obtener_parqueaderosxfacultad(request, facultad_id):
	data = {}
	if facultad_id:
		parqueaderos_facultad = Parqueadero.objects.filter(facultad__id=facultad_id).values_list('id', 'facultad', 'nombre', 'capacidad', 'disponibles')
		data = json.dumps([{"id": p[0], "facultad_id": p[1], "nombre": p[2], "capacidad":p[3], "disponibles":p[4]} for p in parqueaderos_facultad])
	return HttpResponse(data, content_type='application/json')

def obtener_placasxpersona(request, persona_id):
	data = {}
	if persona_id:
		placas_persona = PersonaPlaca.objects.filter(persona__id=persona_id).values_list('placa')
		data = json.dumps([{"nombre": p[0] } for p in placas_persona])
	return HttpResponse(data, content_type='application/json')


def registrar_placa_usuario(request, usuario_id, placa):
	estado = 0
	try:
		placas_persona = PersonaPlaca.objects.filter(persona__id=usuario_id, placa=placa)
		if placas_persona.count() == 0:
			detalle_placa = PersonaPlaca(persona_id=usuario_id, placa=placa)
			detalle_placa.save()
			estado = 1
		else:
			estado = -1
	except Exception as e:
		pass
	
	data = {'estado': estado}
	return HttpResponse(json.dumps(data), content_type='application/json')

def actualizar_estado_parqueo_persona(request, parqueo_id):
	
	hoy = datetime.now()
	reg_parqueo = RegistroParqueo.objects.get(pk=int(parqueo_id))
	if reg_parqueo.estado == 'A': #Activo
		reg_parqueo.estado = 'I' #Marca la Salida
		reg_parqueo.fecha_salida = hoy
		reg_parqueo.hora_salida = hoy.time()
	else: #Inactivo
		reg_parqueo.estado = 'A'
		reg_parqueo.fecha_salida = None
		reg_parqueo.hora_salida = None
	reg_parqueo.save()

	Parqueadero.actualizarDisponibilidadParqueadero(reg_parqueo.parqueadero_id)

	data = {
			"id": reg_parqueo.id, 
			"persona_id": reg_parqueo.usuario_id,
			"facultad_id": reg_parqueo.facultad_id,
			"parqueadero_id":reg_parqueo.parqueadero_id,
			"placa":reg_parqueo.placa,
			"fecha_ingreso":reg_parqueo.fecha_ingreso,
			"hora_ingreso":reg_parqueo.hora_ingreso,
			"fecha_salida":reg_parqueo.fecha_salida,
			"hora_salida":reg_parqueo.hora_salida,
			"longitud":reg_parqueo.longitud,
			"latitud":reg_parqueo.latitud,
			"estado":reg_parqueo.estado
		}

	data = json.dumps(data, cls=DjangoJSONEncoder)
	return HttpResponse(data, content_type='application/json')

def registrar_reporte_parqueos(request, parqueadero_id, num_parqueos_ocupados, usuario_id):
	try:
		#Guarda registro
		reporte = ReportePersona()
		reporte.parqueadero_id = parqueadero_id
		reporte.num_parqueos_ocupados = num_parqueos_ocupados
		reporte.persona_id = usuario_id
		reporte.save()
		estado = 1 #no genera una recompensa inicialmente

		#Se cambia el porcentaje de disponibilidad del parqueadero de ser el caso
		parqueadero = Parqueadero.objects.get(pk=parqueadero_id)

		reportes_todos = ReportePersona.objects.all().order_by('-id')
		num_total_reportes_todos = reportes_todos.count()

		num_ultimos_reportados = num_total_reportes_todos % 5
		cociente = num_total_reportes_todos / 5
		if cociente > 0 and num_ultimos_reportados == 0 :
			total_disponibles = 0
			for reporte in reportes_todos[:5]: # los 5 ultimos elementos
				total_disponibles += reporte.parqueadero.disponibles
			disponibles_promedio = Decimal(total_disponibles)/Decimal(5)
			disponibles_promedio = Decimal(disponibles_promedio.quantize(Decimal('.01'), rounding=ROUND_HALF_UP))
			if parqueadero.disponibles > disponibles_promedio:
				parqueadero.disponibles = int(math.ceil(disponibles_promedio))
			if parqueadero.disponibles < disponibles_promedio:
				parqueadero.disponibles = int(math.floor(disponibles_promedio))
			parqueadero.save()

		# Para generar notificación al usuario de ser el caso que gane recompensa
		reportes_usuario = reportes_todos.filter(persona__id=usuario_id)
		num_total_reportes = reportes_usuario.count()
		rating_actual = num_total_reportes % 5
		cociente = num_total_reportes / 5
		if cociente > 0 and rating_actual == 0 :
			#Se busca el porcentaje disponible de parqueos en un parqueo determinado
			facc = Decimal(parqueadero.disponibles)/Decimal(parqueadero.capacidad) * 100
			porcentaje_disponibles = Decimal(facc.quantize(Decimal('.01'), rounding=ROUND_HALF_UP))
			
			# Se crea una noticia tipo recompensa
			noticia = Noticia()
			noticia.tipo = Noticia.TIPO_NOTICIA_RECOMPENSA
			noticia.descripcion = 'Parqueos disponibles: ' + str(porcentaje_disponibles) + '%'
			noticia.usuario_id = usuario_id
			noticia.save()
			estado = 2 #genera una recompensa a notificar

	except Exception as e:
		print str(e)

	data = {'estado': estado}
	return HttpResponse(json.dumps(data), content_type='application/json')

def obtener_eventos(request):
	data = {}
	noticias = Noticia.objects.filter(tipo=Noticia.TIPO_NOTICIA_EVENTO).order_by('-fecha_creacion').values_list('id', 'descripcion', 'tipo')
	data = json.dumps([{"id": p[0], "descripcion": p[1], "tipo": p[2] } for p in noticias], cls=DjangoJSONEncoder)
	return HttpResponse(data, content_type='application/json')

def obtener_recompensaxusuario(request, usuario_id):
	data = {}
	recompensas = Noticia.objects.filter(tipo=Noticia.TIPO_NOTICIA_RECOMPENSA, usuario__id=usuario_id).order_by('-fecha_creacion').values_list('id', 'descripcion', 'tipo')
	data = json.dumps([{"id": p[0], "descripcion": p[1], "tipo": p[2] } for p in recompensas], cls=DjangoJSONEncoder)
	return HttpResponse(data, content_type='application/json')

def obtener_rating_usuario(request, usuario_id):
	reportes = ReportePersona.objects.filter(persona__id=usuario_id)
	num_total_reportes = reportes.count()
	rating_actual = num_total_reportes % 5
	cociente = num_total_reportes / 5
	if cociente > 0 and rating_actual == 0 :
		rating_actual = 5
	data = {'estado': rating_actual}
	return HttpResponse(json.dumps(data), content_type='application/json')

def verificar_usuario_area_parqueadero(request, usuario_id, longitud, latitud):
	"""
		Comprueba si un punto se encuentra dentro de un polígono
		poligono - Lista de tuplas con los puntos que forman los vértices [(x1, y1), (x2, y2), ..., (xn, yn)]
		x: latitud, y: longitud, poligono : arreglo de puntos del area mapa
	"""
	#Ejemplos San Marino - Area
	# poligono = [(-2.169498,-79.898067), (-2.169876,-79.898641), (-2.168823,-79.898365), (-2.169029,-79.897697)]
	#Punto Dentro del San Marino
	#x = -2.169496
	#y = -79.898451

	#Punto Fuera del San Marino
	#x = -2.169271
	#y = -79.897174
	
	x = latitud
	y = longitud

	# Fiec - Espol Área
	# poligono = [
	# 		# (Latitud, Longitud)
	# 		(-2.144075, -79.967335), #A
	# 		(-2.143985, -79.967079), #B
	# 		(-2.144038, -79.966695), #C
	# 		(-2.144271, -79.966572), #D
	# 		(-2.144641, -79.966769), #E
	# 		(-2.14458, -79.967273), #F
	# 	]

	poligono = [
		(-2.155011, -79.581836), #A
		(-2.154851, -79.581512), #B
		(-2.155813, -79.581402), #C
		(-2.155655, -79.581099), #D
		(-2.155429, -79.581608), #E
		(-2.155301, -79.581276), #F
	]
	
	i = 0
	j = len(poligono) - 1
	en_area = 0
	for i in range(len(poligono)):
		if (poligono[i][1] < y and poligono[j][1] >= y) or (poligono[j][1] < y and poligono[i][1] >= y):
			if poligono[i][0] + (y - poligono[i][1]) / (poligono[j][1] - poligono[i][1]) * (poligono[j][0] - poligono[i][0]) < x:
				en_area = 1
		j = i    
	if en_area == 0:
		parqueadero_id = 1 #Parqueo Fiec
		registros_parqueos_abiertos = RegistroParqueo.objects.filter(usuario__id=usuario_id, parqueadero__id=parqueadero_id, estado='A')
		registros_parqueos_abiertos.update(estado='I')
		Parqueadero.actualizarDisponibilidadParqueadero(parqueadero_id)

	data = {'estado': en_area}
	return HttpResponse(json.dumps(data), content_type='application/json')
