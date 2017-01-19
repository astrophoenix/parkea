# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('backend', '0005_auto_20170114_2308'),
    ]

    operations = [
        migrations.AddField(
            model_name='registroparqueo',
            name='persona',
            field=models.ForeignKey(default='', to='backend.Persona'),
            preserve_default=False,
        ),
    ]
