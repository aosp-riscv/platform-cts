# Copyright (C) 2008 The Android Open Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

.source "T_move_result_object_4.java"
.class  public Ldot/junit/opcodes/move_result_object/d/T_move_result_object_4;
.super  Ljava/lang/Object;


.method public constructor <init>()V
.registers 1

       invoke-direct {v0}, Ljava/lang/Object;-><init>()V
       return-void
.end method

.method public run()V
.registers 16

    invoke-direct {v15} , Ldot/junit/opcodes/move_result_object/d/T_move_result_object_4;->foo()J
    move-result-object v0
    
    return-void
.end method

.method private foo()J
.registers 2

    const-wide v0, 1234
    return-wide v0
.end method


