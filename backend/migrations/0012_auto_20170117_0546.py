# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('backend', '0011_auto_20170117_0543'),
    ]

    operations = [
        migrations.RenameField(
            model_name='registroparqueo',
            old_name='persona',
            new_name='usuario',
        ),
    ]
