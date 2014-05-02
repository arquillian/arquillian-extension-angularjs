/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
window.Graphene = window.Graphene || {};

window.Graphene.angularActionSearchContext = (function() {
  var prefixes = ['ng-', 'ng_', 'data-ng-', 'x-ng-', 'ng\\:'];

  return {
    findElements: function(action) {
      return window.Graphene.angularActionSearchContext.findElementsInElement(action, document);
    },
    findElementsInElement: function(action, root) {
      for (var p = 0; p < prefixes.length; ++p) {
        var selector = '[' + prefixes[p] + 'click="' + action + '"]';
        var elements = root.querySelectorAll(selector);
        if (elements.length) {
          return window.Graphene.arrayConverter(elements);
        }
      }

      for (var p = 0; p < prefixes.length; ++p) {
        var selector = '[' + prefixes[p] + 'submit="' + action + '"]';
        var elements = root.querySelectorAll(selector);
        if (elements.length) {
          return window.Graphene.arrayConverter(elements);
        }
      }
    }
  }

})();