# coding=utf-8
from django.shortcuts import render
from models import *
from django.core import serializers
from django.http import HttpResponseForbidden, HttpResponseRedirect, HttpResponse
import json
from urllib import unquote
from django.db import transaction
from django.db.models import Count
from datetime import datetime, date
from django.core.serializers.json import DjangoJSONEncoder
from decimal import *
import math

def validar_login(request, correo, clave):
	"""
	Verifica si un usuario existe en el sistema por medio de su correo y clave
	"""
	personas = Persona.objects.filter(correo=unquote(correo), clave=unquote(clave))
	if personas.count() == 1:
		persona = personas.first()
		estado = persona.id
	else:
		estado = 0
	data = {'estado': estado}
	return HttpResponse(json.dumps(data), content_type='application/json')

def obtener_usuario(request, usuario_id):
	"""
	Consulta y devuelve los datos de un usuario por su id
	"""
	usuario = Persona.objects.get(pk=usuario_id)
	data = {'nombre': usuario.nombre, 'apellido': usuario.apellido, 'correo':usuario.correo}
	return HttpResponse(json.dumps(data), content_type='application/json')

def registrar_usuario(request, nombre, apellido, placa, correo, clave):
	"""
	Agrega un nuevo usuario al sistema con los diferentes datos
	"""
	# Valida en el sitio oficial de atm si existe la placa en su base de datos
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
			estado = persona.id
			if placa != '---':
				estado_placa = PersonaPlaca.validarPlaca(placa)
				if estado_placa == 'OK':
					detalle_placa = PersonaPlaca(persona=persona, placa=placa)
					detalle_placa.save()
				else:
					#Placa No válida
					estado = -1
		except Exception as e:
			estado = 0	
	else:
		# Existen Personas previas con el usuario.
			estado = 0
	data = {'estado': estado}
	return HttpResponse(json.dumps(data), content_type='application/json')

def registrar_parqueo_persona(request, parqueadero_id, placa, longitud, latitud, usuario_id):
	"""
	Guarda un nuevo parqueo con la placa las coordenadas del punto y el usuario.
	"""
	estado = 0
	if parqueadero_id and placa:
		try:
			parqueadero = Parqueadero.objects.get(pk=parqueadero_id)
			#valida si existen parqueos ocupados previamente por el usuario
			registros_parqueos_abiertos = RegistroParqueo.objects.filter(usuario__id=usuario_id, parqueadero__id=parqueadero_id, estado='A')
			if registros_parqueos_abiertos.count() == 0:
				if parqueadero.disponibles > 0:
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
				else:
					estado = -2 # No hay capacidad
			else:
				estado = -1
		except Exception as e:
			pass
	data = {'estado': estado}
	return HttpResponse(json.dumps(data), content_type='application/json')

def obtener_facultades(request):
	"""
	Consulta y devuelve las diferentes facultades registradas.
	"""
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
	"""
	Consulta y devuelve una lista de parqueos del ususario
	"""
	data = {}
	parqueos_persona = RegistroParqueo.objects.filter(usuario__id=int(usuario_id)).order_by('-id').values_list('id', 'usuario', 'facultad', 'parqueadero', 'placa', 'fecha_ingreso', 'hora_ingreso', 'fecha_salida', 'hora_salida', 'longitud', 'latitud', 'estado')
	data = json.dumps([{"id": p[0], "persona_id": p[1], "facultad_id": p[2], "parqueadero_id":p[3], "placa":p[4], "fecha_ingreso":p[5], "hora_ingreso":p[6], "fecha_salida":p[7], "hora_salida":p[8], "longitud":p[9], "latitud":p[10], "estado":p[11] } for p in parqueos_persona], cls=DjangoJSONEncoder)
	return HttpResponse(data, content_type='application/json')

def obtener_parqueaderosxfacultad(request, facultad_id):
	"""
	Consulta y devuelve los diferentes parqueos de cada facultad
	"""
	data = {}
	if facultad_id:
		parqueaderos_facultad = Parqueadero.objects.filter(facultad__id=facultad_id).values_list('id', 'facultad', 'nombre', 'capacidad', 'disponibles')
		data = json.dumps([{"id": p[0], "facultad_id": p[1], "nombre": p[2], "capacidad":p[3], "disponibles":p[4]} for p in parqueaderos_facultad])
	return HttpResponse(data, content_type='application/json')

def obtener_placasxpersona(request, persona_id):
	"""
	Consulta y devuelve las diferentes placas asociadas al usuario(persona).
	"""
	data = {}
	if persona_id:
		placas_persona = PersonaPlaca.objects.filter(persona__id=persona_id).values_list('placa')
		data = json.dumps([{"nombre": p[0] } for p in placas_persona])
	return HttpResponse(data, content_type='application/json')

def registrar_placa_usuario(request, usuario_id, placa):
	"""
	Guarda una nueva placa registrada por el usuario.
	"""
	estado = 0
	try:
		# Valida en el sitio oficial de atm si existe la placa en su base de datos
		estado_placa = PersonaPlaca.validarPlaca(placa)
		if estado_placa == 'OK':
			placas_persona = PersonaPlaca.objects.filter(persona__id=usuario_id, placa=placa)
			if placas_persona.count() == 0: #Placa nueva del usuario
				detalle_placa = PersonaPlaca(persona_id=usuario_id, placa=placa)
				detalle_placa.save()
				estado = 1
			else: #Placa ya existe para el usuario
				estado = -1
		else:
			estado = -2 #Placa no valida para la ATM
	except Exception as e:
		pass
	
	data = {'estado': estado}
	return HttpResponse(json.dumps(data), content_type='application/json')

def actualizar_estado_parqueo_persona(request, parqueo_id):
	"""
	Modifica el estado de un parqueo (Activo o Inactivo).
	Activo cuando el usuario a registrado el parqueo
	Inactivo cuando el usuario a salido del area 
	"""
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
	"""
	Guarda un reporte de parqueo y se habilita un nuevo registro cada hora despues del ultimo reporte.
	"""
	try:
		estado = 0
		registrar = False
		diferencia_segundos = 0
		hoy = datetime.now()
		horario_fin_dia = datetime.strptime("18:00", "%H:%M")
		if hoy.time() <= horario_fin_dia.time():
			# Verifica el ultimo reporte del dia del usuario y compara si ha pasado una hora para permitirle 
			# registrar otro reporte
			ultimo_reporte = ReportePersona.objects.filter(fecha_creacion=hoy, persona_id=usuario_id).last()
			if ultimo_reporte:
				diferencia = datetime.combine(date.min, hoy.time()) - datetime.combine(date.min, ultimo_reporte.hora_creacion)
				diferencia_segundos = diferencia.total_seconds()
				if diferencia_segundos >= 3600:
					registrar = True
			else:
				registrar = True
			
			if registrar:
				#Guarda Reporte Persona
				reporte = ReportePersona()
				reporte.parqueadero_id = parqueadero_id
				reporte.num_parqueos_ocupados = num_parqueos_ocupados
				reporte.persona_id = usuario_id
				reporte.fecha_creacion = hoy
				reporte.hora_creacion = hoy.time()
				reporte.save()
				estado = 1
			else:
				estado = -1 # No puede reportar porque hace menos de 1 hora ha reportado.
		else:
			#No puede reportar pasado las 18:00
			estado = -2
	except Exception as e:
		print str(e)

	data = {'estado': estado}
	return HttpResponse(json.dumps(data), content_type='application/json')

def obtener_notificaciones_recompensa(request, usuario_id):
	"""
	Se busca una notificación pendiente de entregar al usuario
	"""
	estado = 0
	try:
		recompensas = Noticia.objects.filter(tipo=Noticia.TIPO_NOTICIA_RECOMPENSA, usuario__id=usuario_id, estado='P')
		ultima_recompensa = recompensas.last()
		parqueadero_fiec = Parqueadero.objects.get(pk=1)
		ultima_recompensa.descripcion = 'Parqueos disponibles: ' + str(parqueadero_fiec.disponibles)
		ultima_recompensa.save()
		if recompensas.count() >= 1:
			recompensas.update(estado='E')
			estado = 1
	except Exception as e:
		estado = 0
	
	data = {'estado': estado}
	return HttpResponse(json.dumps(data), content_type='application/json')

def obtener_eventos(request):
	"""
	Consulta y devuelve una lista de eventos.
	"""
	data = {}
	noticias = Noticia.objects.filter(tipo=Noticia.TIPO_NOTICIA_EVENTO).order_by('-fecha_creacion').values_list('id', 'descripcion', 'tipo')
	data = json.dumps([{"id": p[0], "descripcion": p[1], "tipo": p[2] } for p in noticias], cls=DjangoJSONEncoder)
	return HttpResponse(data, content_type='application/json')

def obtener_recompensaxusuario(request, usuario_id):
	"""
	Consulta y devuelve una lista de las recompensas ganadas por el usuario.
	"""
	data = {}
	recompensas = Noticia.objects.filter(tipo=Noticia.TIPO_NOTICIA_RECOMPENSA, usuario__id=usuario_id).order_by('-fecha_creacion').values_list('id', 'descripcion', 'tipo')
	data = json.dumps([{"id": p[0], "descripcion": p[1], "tipo": p[2] } for p in recompensas], cls=DjangoJSONEncoder)
	return HttpResponse(data, content_type='application/json')

def obtener_rating_usuario(request, usuario_id):
	"""
	Calcula el valor del rating en funcion del numero de veces que reporta el usuario en el horario 7:00 a 18:00
	"""
	# Relación Reporte usuario vs estrella rating
	# 1 ReporteUsuario = 1/2 Estrella
	hoy = datetime.now()
	reportes = ReportePersona.objects.filter(persona__id=usuario_id, fecha_creacion=hoy)
	num_total_reportes = reportes.count()
	rating_actual = Decimal(num_total_reportes) / Decimal(2)
	rating_actual = str(Decimal(rating_actual.quantize(Decimal('.01'), rounding=ROUND_HALF_UP)))
	data = {'estado': rating_actual}
	return HttpResponse(json.dumps(data), content_type='application/json')

def verificar_registro_reporte_parqueo_activo(request, usuario_id):
	estado = 0
	hoy = datetime.now()
	parqueadero_id = 1 #Parqueo Fiec
	reportes = ReportePersona.objects.filter(persona__id=usuario_id, fecha_creacion=hoy)
	parqueos_abiertos = RegistroParqueo.objects.filter(usuario__id=usuario_id, parqueadero__id=parqueadero_id, estado='A')
	if reportes.count() > 0 or parqueos_abiertos.count() > 0:
		estado = 1
	data = {'estado': estado}
	return HttpResponse(json.dumps(data), content_type='application/json')

def verificar_usuario_area_parqueadero(request, usuario_id, longitud, latitud):
	"""
		Comprueba si un punto se encuentra dentro de un polígono
		poligono - Lista de tuplas con los puntos que forman los vértices [(x1, y1), (x2, y2), ..., (xn, yn)]
		x: latitud, y: longitud, poligono : arreglo de puntos del area mapa
	"""
	# ESPOL COORDENADAS AREA
	# poligono = [
	# 	(-2.143713,-79.967327),
	# 	(-2.143682,-79.967121),
	# 	(-2.143736,-79.966918),
	# 	(-2.143803,-79.966744),
	# 	(-2.143919,-79.966609),
	# 	(-2.144067,-79.966508),
	# 	(-2.144244,-79.966410),
	# 	(-2.144438,-79.966405),
	# 	(-2.144615,-79.966446),
	# 	(-2.144760,-79.966547),
	# 	(-2.144828,-79.966686),
	# 	(-2.144858,-79.966849),
	# 	(-2.144818,-79.966996),
	# 	(-2.144772,-79.967136),
	# 	(-2.144670,-79.967245),
	# 	(-2.144519,-79.967376),
	# 	(-2.144398,-79.967478),
	# 	(-2.144247,-79.967569),
	# 	(-2.144080,-79.967660),
	# 	(-2.143886,-79.967549)
	# ]

	# AREA TEST COORDENADAS
	# poligono = [
	# 	(-2.220546, -79.920717),
	# 	(-2.220852, -79.920119),
	# 	(-2.220852, -79.920119),
	# 	(-2.221666, -79.921179)
	# ]

	# AREA AMY COORDENADAS
	# poligono = [
	# 	(-2.155484, -79.580791), 
	# 	(-2.155484, -79.580791),
	# 	(-2.155924, -79.581639),
	# 	(-2.155052, -79.582127)
	# ]

	# AREA BIBLIOTECA ESPOL
	poligono = [
		(-2.146501, -79.966501),
		(-2.146723, -79.966586),
		(-2.147041, -79.966678),
		(-2.147337, -79.966737),
		(-2.147561, -79.966751),
		(-2.147723, -79.966612),
		(-2.147818, -79.966114),
		(-2.147648, -79.965607),
		(-2.147276, -79.965520),
		(-2.146927, -79.965554),
		(-2.146672, -79.965651),
		(-2.146460, -79.965848),
		(-2.146342, -79.966104)
	]

	x = float(latitud)
	y = float(longitud)

	i = 0
	j = len(poligono) - 1
	en_area = 0
	for i in range(len(poligono)):
		if (poligono[i][1] < y and poligono[j][1] >= y) or (poligono[j][1] < y and poligono[i][1] >= y):
			if poligono[i][0] + (y - poligono[i][1]) / (poligono[j][1] - poligono[i][1]) * (poligono[j][0] - poligono[i][0]) < x:
				en_area = 1
		j = i    
	#En caso de no estar en el area cierra todos los parqueos abiertos con fecha y hora que ha detectado el servidor 
	#cuando se logueó o estuvo activa la app
	if en_area == 0:
		hoy = datetime.now()
		parqueadero_id = 1 #Parqueo Fiec
		registros_parqueos_abiertos = RegistroParqueo.objects.filter(usuario__id=usuario_id, parqueadero__id=parqueadero_id, estado='A')
		registros_parqueos_abiertos.update(estado='I', fecha_salida=hoy, hora_salida=hoy.time())
		Parqueadero.actualizarDisponibilidadParqueadero(parqueadero_id)

	data = {'estado': en_area}
	return HttpResponse(json.dumps(data), content_type='application/json')

# def verificar_cron(request):
	# hoy = datetime.now()
	# hora_inicio = datetime.strptime('07:00', '%H:%M').time()
	# hora_fin = hoy.time()
	# reportes_dia = ReportePersona.objects.filter(fecha_creacion=hoy, hora_creacion__range=[hora_inicio, hora_fin]).order_by('hora_creacion')
	# reportes_dia = reportes_dia.values_list('persona').annotate(num_max_reportes=Count('persona')).order_by('-num_max_reportes')
	# usuario_id, num_reportes = reportes_dia[0]
	
	# #Se crea la Recompensa
	# noticia = Noticia()
	# noticia.tipo = Noticia.TIPO_NOTICIA_RECOMPENSA
	# noticia.descripcion = ""
	# noticia.usuario_id = usuario_id
	# noticia.fecha_creacion = hoy
	# noticia.hora_creacion = hoy.time()
	# noticia.save()