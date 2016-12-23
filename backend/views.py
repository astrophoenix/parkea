from django.shortcuts import render
from models import *
from django.core import serializers
from django.http import HttpResponseForbidden, HttpResponseRedirect, HttpResponse

# Create your views here.
def validar_login(request):
	foos = Persona.objects.all()
	data = serializers.serialize('json', foos)
	return HttpResponse(data, content_type='application/json')
