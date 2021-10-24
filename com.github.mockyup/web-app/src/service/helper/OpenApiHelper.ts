import { Component, Schema } from '@/service/webclient/model/openapi/embeddable/OpenApiEmbedded'

export const OpenApiHelper = {
  getSimpleRef (ref: string): string {
    if (ref === null || ref === undefined) {
      return ref
    }
    try {
      if (ref.startsWith('#/components/')) {
        ref = ref.substring(ref.lastIndexOf('/') + 1)
      }
    } catch (exception) {
      console.log('exception')
    }
    return ref
  },
  getSchemaFromName (ref: string, component: Component): Schema {
    return component.schemas[this.getSimpleRef(ref)]
  },
  cleansingNullAttributes (properties: any, component: Component): any {
    if (isEmpty(properties)) {
      return properties
    }
    for (const name in properties) {
      if (properties[name] === null || properties[name] === undefined) {
        delete properties[name]
      }
      if (isEmpty(properties[name])) {
        delete properties[name]
      }
      if (Array.isArray(properties[name]) && properties[name].length === 0) {
        delete properties[name]
      }
      if (typeof properties[name] === 'object' && name !== 'schema' && name !== 'properties' && properties[name] !== null && properties[name] !== undefined) {
        properties[name] = this.cleansingNullAttributes(properties[name], component)
      }
      if (name === 'schema' && (properties[name] !== undefined && properties[name] !== null)) {
        properties.schema = this.cleansingNullAttributes(properties[name], component)
      }
      if (name === 'properties' && (properties[name] !== undefined && properties[name] !== null)) {
        properties.properties = this.cleansingNullProperties(properties[name], component)
      }
    }
    return properties
  },
  cleansingNullProperties (properties: any, component: Component): any {
    if (isEmpty(properties)) {
      return properties
    }
    for (const propName in properties) {
      for (const name in properties[propName]) {
        if (properties[propName][name] === null || properties[propName][name] === undefined) {
          delete properties[propName][name]
        }
        if (isEmpty(properties[propName][name])) {
          delete properties[propName][name]
        }
        if (name === '$ref' && (properties[propName][name] !== undefined && properties[propName][name] !== null)) {
          const childProperties = this.getSchemaFromName(this.getSimpleRef(properties[propName][name]), component)
          if (!isEmpty(childProperties.properties)) {
            properties[propName].properties = this.cleansingNullProperties(childProperties.properties, component)
            delete properties[propName][name]
          }
        }
        if (name === 'xml' && (properties[propName][name] !== undefined && properties[propName][name] !== null)) {
          properties[propName] = this.cleansingNullProperties(properties[propName], component)
        }
      }
    }
    return properties
  }
}

export const isEmpty = obj => {
  for (const prop in obj) {
    if (Object.prototype.hasOwnProperty.call(obj, prop)) {
      return false
    }
  }
  return JSON.stringify(obj) === JSON.stringify({})
}
