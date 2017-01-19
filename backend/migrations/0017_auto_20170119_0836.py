# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations
import datetime
from django.utils.timezone import utc


class Migration(migrations.Migration):

    dependencies = [
        ('backend', '0016_noticia_usuario'),
    ]

    operations = [
        migrations.AddField(
            model_name='reportepersona',
            name='fecha_creacion',
            field=models.DateTimeField(default=datetime.datetime(2017, 1, 19, 8, 36, 14, 816941, tzinfo=utc), auto_now_add=True),
            preserve_default=False,
        ),
        migrations.AddField(
            model_name='reportepersona',
            name='ultima_modificacion',
            field=models.DateTimeField(default=datetime.datetime(2017, 1, 19, 8, 36, 24, 515652, tzinfo=utc), auto_now=True),
            preserve_default=False,
        ),
    ]
