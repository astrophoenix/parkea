from backend.models import *
from django.db.models import Count
from datetime import datetime

def verificarGanadorRecompensa():
	hoy = datetime.now()
	hora_inicio = datetime.strptime('07:00', '%H:%M').time()
	hora_fin = hoy.time()
	reportes_dia = ReportePersona.objects.filter(fecha_creacion=hoy, hora_creacion__range=[hora_inicio, hora_fin]).order_by('hora_creacion')
	reportes_dia = reportes_dia.values_list('persona').annotate(num_max_reportes=Count('persona')).order_by('-num_max_reportes')
	usuario_id, num_reportes = reportes_dia[0]
	
	#Se crea la Recompensa
	noticia = Noticia()
	noticia.tipo = Noticia.TIPO_NOTICIA_RECOMPENSA
	noticia.descripcion = ""
	noticia.usuario_id = usuario_id
	noticia.fecha_creacion = hoy
	noticia.hora_creacion = hoy.time()
	noticia.save()