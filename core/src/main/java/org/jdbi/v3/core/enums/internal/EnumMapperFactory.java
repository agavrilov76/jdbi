/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jdbi.v3.core.enums.internal;

import java.util.Optional;

import org.jdbi.v3.core.config.ConfigRegistry;
import org.jdbi.v3.core.enums.EnumStrategy;
import org.jdbi.v3.core.generic.GenericTypes;
import org.jdbi.v3.core.internal.EnumStrategies;
import org.jdbi.v3.core.mapper.ColumnMapper;
import org.jdbi.v3.core.mapper.EnumMapper;
import org.jdbi.v3.core.mapper.QualifiedColumnMapperFactory;
import org.jdbi.v3.core.qualifier.QualifiedType;

public class EnumMapperFactory implements QualifiedColumnMapperFactory {
    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Optional<ColumnMapper<?>> build(QualifiedType<?> givenType, ConfigRegistry config) {
        return Optional.of(givenType.getType())
            .map(GenericTypes::getErasedType)
            .filter(Enum.class::isAssignableFrom)
            .map(clazz -> makeEnumArgument((QualifiedType<Enum>) givenType, (Class<Enum>) clazz, config));
    }

    private static <E extends Enum<E>> ColumnMapper<?> makeEnumArgument(QualifiedType<E> givenType, Class<E> enumClass, ConfigRegistry config) {
        boolean byName = EnumStrategy.BY_NAME == config.get(EnumStrategies.class).findStrategy(givenType);

        return byName
            ? EnumMapper.byName(enumClass)
            : EnumMapper.byOrdinal(enumClass);
    }
}
