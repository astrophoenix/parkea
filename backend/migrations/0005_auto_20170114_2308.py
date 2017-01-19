# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('backend', '0004_registroparqueo'),
    ]

    operations = [
        migrations.AddField(
            model_name='registroparqueo',
            name='latitud',
            field=models.DecimalField(null=True, max_digits=13, decimal_places=10),
        ),
        migrations.AddField(
            model_name='registroparqueo',
            name='longitud',
            field=models.DecimalField(null=True, max_digits=13, decimal_places=10),
        ),
    ]
