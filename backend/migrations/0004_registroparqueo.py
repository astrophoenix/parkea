# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('backend', '0003_parqueadero'),
    ]

    operations = [
        migrations.CreateModel(
            name='registroParqueo',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('placa', models.CharField(max_length=13, null=True)),
                ('fecha_ingreso', models.DateField(null=True)),
                ('hora_ingreso', models.TimeField(null=True)),
                ('fecha_salida', models.DateField(null=True)),
                ('hora_salida', models.TimeField(null=True)),
                ('facultad', models.ForeignKey(to='backend.Facultad', null=True)),
                ('parqueadero', models.ForeignKey(to='backend.Parqueadero', null=True)),
            ],
        ),
    ]
