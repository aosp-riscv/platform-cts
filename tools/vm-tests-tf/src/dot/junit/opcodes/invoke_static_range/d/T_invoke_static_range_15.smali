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

.source "T_invoke_static_range_15.java"
.class  public Ldot/junit/opcodes/invoke_static_range/d/T_invoke_static_range_15;
.super  Ljava/lang/Object;


.method public constructor <init>()V
.registers 2

       invoke-direct {v1}, Ljava/lang/Object;-><init>()V
       return-void
.end method

.method public run()Z
.registers 6

    const v1, 123
    const v2, 345

    const v3, 12
    const v4, 6
    
    invoke-static/range {v3..v4}, Ldot/junit/opcodes/invoke_static_range/TestClass;->testArgsOrder(II)I

    move-result v3
    const v4, 2
    if-ne v3, v4, :Label0

    const v4, 123
    if-ne v1, v4, :Label0

    const v4, 345
    if-ne v2, v4, :Label0

    const v0, 1
    return v0

:Label0
    const v0, 0
    return v0
.end method


