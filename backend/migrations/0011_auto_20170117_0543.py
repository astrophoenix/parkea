# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('backend', '0010_auto_20170117_0232'),
    ]

    operations = [
        migrations.AlterField(
            model_name='registroparqueo',
            name='persona',
            field=models.ForeignKey(to='backend.Persona', null=True),
        ),
    ]
