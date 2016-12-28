# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('backend', '0001_initial'),
    ]

    operations = [
        migrations.AddField(
            model_name='persona',
            name='clave',
            field=models.CharField(max_length=20, null=True),
        ),
        migrations.AlterField(
            model_name='persona',
            name='apellido',
            field=models.CharField(max_length=30, null=True),
        ),
        migrations.AlterField(
            model_name='persona',
            name='correo',
            field=models.CharField(max_length=100, null=True),
        ),
        migrations.AlterField(
            model_name='persona',
            name='nombre',
            field=models.CharField(max_length=30, null=True),
        ),
        migrations.AlterField(
            model_name='persona',
            name='tipo',
            field=models.CharField(default=b'V', max_length=2, choices=[(b'E', b'Estudiante'), (b'V', b'Visitante')]),
        ),
    ]
