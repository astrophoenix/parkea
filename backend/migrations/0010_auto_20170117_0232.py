# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('backend', '0009_registroparqueo_persona'),
    ]

    operations = [
        migrations.AlterField(
            model_name='registroparqueo',
            name='persona',
            field=models.ForeignKey(blank=True, to='backend.Persona', null=True),
        ),
    ]
