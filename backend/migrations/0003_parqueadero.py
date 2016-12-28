# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('backend', '0002_auto_20161224_0511'),
    ]

    operations = [
        migrations.CreateModel(
            name='Parqueadero',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('nombre', models.CharField(max_length=30)),
                ('capacidad', models.IntegerField(null=True)),
                ('disponibles', models.IntegerField(null=True)),
                ('facultad', models.ForeignKey(to='backend.Facultad', null=True)),
            ],
        ),
    ]
