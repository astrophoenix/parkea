# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('backend', '0008_remove_registroparqueo_persona'),
    ]

    operations = [
        migrations.AddField(
            model_name='registroparqueo',
            name='persona',
            field=models.ForeignKey(to='backend.Persona', null=True),
        ),
    ]
