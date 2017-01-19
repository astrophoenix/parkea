# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('backend', '0013_registroparqueo_estado'),
    ]

    operations = [
        migrations.CreateModel(
            name='ReportePersona',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('num_parqueos_ocupados', models.IntegerField(null=True)),
                ('parqueadero', models.ForeignKey(to='backend.Parqueadero', null=True)),
                ('persona', models.ForeignKey(to='backend.Persona', null=True)),
            ],
        ),
    ]
