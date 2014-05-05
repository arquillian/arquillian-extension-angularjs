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

window.Graphene.angularRepeatSearchContext = (function() {
  var prefixes = ['ng-', 'ng_', 'data-ng-', 'x-ng-', 'ng\\:'];

  return {
    findElements: function(repeaterText) {
      return window.Graphene.angularRepeatSearchContext.findElementsInElement(repeaterText, document);
    },
    findElementsInElement: function(repeaterText, root) {
      var rows = [];
      for (var p = 0; p < prefixes.length; ++p) {
        var attr = prefixes[p] + 'repeat';
        var repeatElems = root.querySelectorAll('[' + attr + ']');
        attr = attr.replace(/\\/g, '');
        for (var i = 0; i < repeatElems.length; ++i) {
          if (repeatElems[i].getAttribute(attr).indexOf(repeaterText) != -1) {
            rows.push(repeatElems[i]);
          }
        }
      }
      for (var p = 0; p < prefixes.length; ++p) {
        var attr = prefixes[p] + 'repeat-start';
        var repeatElems = root.querySelectorAll('[' + attr + ']');
        attr = attr.replace(/\\/g, '');
        for (var i = 0; i < repeatElems.length; ++i) {
          if (repeatElems[i].getAttribute(attr).indexOf(repeaterText) != -1) {
            var elem = repeatElems[i];
            while (elem.nodeType != 8 ||
              elem.nodeValue.indexOf(repeaterText) == -1) {
              rows.push(elem);
              elem = elem.nextSibling;
            }
          }
        }
      }
      return window.Graphene.arrayConverter(rows);
    }
  }

})();