# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('backend', '0007_auto_20170117_0218'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='registroparqueo',
            name='persona',
        ),
    ]
