# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('backend', '0015_noticia'),
    ]

    operations = [
        migrations.AddField(
            model_name='noticia',
            name='usuario',
            field=models.ForeignKey(to='backend.Persona', null=True),
        ),
    ]
