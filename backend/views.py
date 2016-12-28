from django.shortcuts import render
from models import *
from django.core import serializers
from django.http import HttpResponseForbidden, HttpResponseRedirect, HttpResponse
import json
from urllib import unquote
from django.db import transaction
from datetime import datetime

def validar_login(request, correo, clave):
	personas = Persona.objects.filter(correo=unquote(correo), clave=unquote(clave))
	if personas.count() == 1:
		estado = 1
	else:
		estado = 0
	data = {'estado': estado}
	return HttpResponse(json.dumps(data), content_type='application/json')

def registrar_usuario(request, nombre, apellido, correo, clave):
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
			estado = 1
			persona.save()
		except Exception as e:
			estado = 0	
	else:
		# Existen Personas previas con el usuario.
		estado = 0
	data = {'estado': estado}
	return HttpResponse(json.dumps(data), content_type='application/json')

def registrar_parqueo_persona(request, parqueadero_id, placa):
	estado = 0
	if parqueadero_id and placa:
		try:
			hoy = datetime.now()
			parqueadero = Parqueadero.objects.get(pk=parqueadero_id)		
			reg_parqueo = registroParqueo()
			reg_parqueo.facultad = parqueadero.facultad
			reg_parqueo.parqueadero = parqueadero
			reg_parqueo.placa = placa
			reg_parqueo.fecha_ingreso = hoy
			reg_parqueo.hora_ingreso = hoy.time() 
			reg_parqueo.save()
			estado = 1
		except Exception as e:
			print str(e)
			pass
	data = {'estado': estado}
	return HttpResponse(json.dumps(data), content_type='application/json')

def obtener_facultades(request):
	facultades = Facultad.objects.values_list('id', 'nombre')
	data = json.dumps([{"id": f[0], "nombre": f[1]} for f in facultades])
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


