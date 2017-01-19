# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('backend', '0012_auto_20170117_0546'),
    ]

    operations = [
        migrations.AddField(
            model_name='registroparqueo',
            name='estado',
            field=models.CharField(max_length=2, null=True),
        ),
    ]
