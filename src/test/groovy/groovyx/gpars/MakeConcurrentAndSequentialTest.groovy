// GPars - Groovy Parallel Systems
//
// Copyright © 2008-11  The original author or authors
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//       http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.


package groovyx.gpars

import static groovyx.gpars.GParsPool.withPool

/**
 * Author: Vaclav Pech
 */

class MakeConcurrentAndSequentialTest extends GroovyTestCase {

    public void testIsConcurrencyActive() {
        def items1 = [1, 2, 3, 4, 5]
        withPool {
            shouldFail(MissingMethodException) {
                items1.isConcurrencyActive()
            }
            def items2 = [1, 2, 3, 4, 5]
            assert items2.makeConcurrent().isConcurrencyActive()
            assert items2.makeConcurrent().makeConcurrent().isConcurrencyActive()
        }
    }

    public void testMakeSequential() {
        def items1 = [1, 2, 3, 4, 5]
        withPool {
            items1.makeSequential()

            def items2 = [1, 2, 3, 4, 5]
            assert !items2.makeConcurrent().makeSequential().isConcurrencyActive()
            assert items2.makeConcurrent().makeSequential().makeConcurrent().isConcurrencyActive()
        }
    }

    public void testMakeSequentialDirectly() {
        def items1 = [1, 2, 3, 4, 5]
        withPool {
            items1.makeSequential()
            def result = [].asSynchronized()
            items1.each {
                sleep 100; result << Thread.currentThread()
            }
            assert result.unique().size() == 1
        }
    }

    public void testAsConcurrent() {
        def items1 = [1, 2, 3, 4, 5]
        withPool {
            items1.asConcurrent {
                assert items1.isConcurrent()
                assert items1.isConcurrencyActive()
                assert it.isConcurrent()
                assert it.isConcurrencyActive()
            }
            assert items1.isConcurrent()
            assert !items1.isConcurrencyActive()
        }
    }

    public void testIsConcurrencyActiveWithString() {
        def items1 = '1abc'
        withPool {
            shouldFail(MissingMethodException) {
                items1.isConcurrencyActive()
            }

            def items2 = '2abc'
            assertTrue items2.makeConcurrent().isConcurrencyActive()
            assertTrue items2.makeConcurrent().makeConcurrent().isConcurrencyActive()
        }
    }

    public void testMakeConcurrentEach() {
        def items1 = [1, 2, 3, 4, 5]
        withPool {
            items1.makeConcurrent()
            def result = [].asSynchronized()
            items1.each {
                sleep 100; result << Thread.currentThread()
            }
            assert result.unique().size() > 1
        }
    }

    public void testMakeSequentialEach() {
        def items1 = [1, 2, 3, 4, 5]
        withPool {
            items1.makeConcurrent().makeSequential()
            def result = [].asSynchronized()
            items1.each {
                sleep 100; result << Thread.currentThread()
            }
            assert result.unique().size() == 1
        }
    }

    public void testAsConcurrentEach() {
        def items1 = [1, 2, 3, 4, 5]
        withPool {
            items1.asConcurrent {
                def result = [].asSynchronized()
                items1.each {
                    sleep 100; result << Thread.currentThread()
                }
                assert result.unique().size() > 1
            }
        }
    }

    public void testAsConcurrentSequentialEach() {
        def items1 = [1, 2, 3, 4, 5]
        withPool {
            items1.asConcurrent {
                items1.each {}
            }
            def result = [].asSynchronized()
            items1.each {
                sleep 100; result << Thread.currentThread()
            }
            assert result.unique().size() == 1
        }
    }

    public void testMakeConcurrentEachOnString() {
        def items1 = '12abc'
        withPool {
            items1.makeConcurrent()
            def result = [].asSynchronized()
            items1.each {
                sleep 100; result << Thread.currentThread()
            }
            assert result.unique().size() > 1
        }
    }

    public void testMakeSequentialEachOnString() {
        def items1 = '13abc'
        withPool {
            items1.makeConcurrent().makeSequential()
            def result = [].asSynchronized()
            items1.each {
                sleep 100; result << Thread.currentThread()
            }
            assert result.unique().size() == 1
        }
    }

    public void testAsConcurrentEachOnString() {
        def items1 = '14abc'
        withPool {
            items1.asConcurrent {
                def result = [].asSynchronized()
                items1.each {
                    sleep 100; result << Thread.currentThread()
                }
                assert result.unique().size() > 1
            }
        }
    }

    public void testAsConcurrentSequentialEachOnString() {
        def items1 = '15abc'
        withPool {
            items1.asConcurrent {
                items1.each {}
            }
            def result = [].asSynchronized()
            items1.each {
                sleep 100; result << Thread.currentThread()
            }
            assert result.unique().size() == 1
        }
    }

    public void testMakeConcurrentCollect() {
        def items1 = [1, 2, 3, 4, 5]
        withPool {
            items1.makeConcurrent()
            def result = [].asSynchronized()
            items1.collect {
                sleep 100; result << Thread.currentThread()
            }
            assert result.unique().size() > 1
        }
    }

    public void testMakeSequentialCollect() {
        def items1 = [1, 2, 3, 4, 5]
        withPool {
            items1.makeConcurrent().makeSequential()
            def result = [].asSynchronized()
            items1.collect {
                sleep 100; result << Thread.currentThread()
            }
            assert result.unique().size() == 1
        }
    }

    public void testAsConcurrentCollect() {
        def items1 = [1, 2, 3, 4, 5]
        withPool {
            items1.asConcurrent {
                def result = [].asSynchronized()
                items1.collect {
                    sleep 100; result << Thread.currentThread()
                }
                assert result.unique().size() > 1
            }
        }
    }

    public void testAsConcurrentSequentialCollect() {
        def items1 = [1, 2, 3, 4, 5]
        withPool {
            items1.asConcurrent {
                items1.collect {}
            }
            def result = [].asSynchronized()
            items1.collect {
                sleep 100; result << Thread.currentThread()
            }
            assert result.unique().size() == 1
        }
    }

    public void testMakeConcurrentFindAll() {
        def items1 = [1, 2, 3, 4, 5]
        withPool {
            items1.makeConcurrent()
            def result = [].asSynchronized()
            items1.findAll {
                sleep 100; result << Thread.currentThread()
            }
            assert result.unique().size() > 1
        }
    }

    public void testMakeSequentialFindAll() {
        def items1 = [1, 2, 3, 4, 5]
        withPool {
            items1.makeConcurrent().makeSequential()
            def result = [].asSynchronized()
            items1.findAll {
                sleep 100; result << Thread.currentThread()
            }
            assert result.unique().size() == 1
        }
    }

    public void testAsConcurrentFindAll() {
        def items1 = [1, 2, 3, 4, 5]
        withPool {
            items1.asConcurrent {
                def result = [].asSynchronized()
                items1.findAll {
                    sleep 100; result << Thread.currentThread()
                    true
                }
                assert result.unique().size() > 1
            }
        }
    }

    public void testAsConcurrentSequentialFindAll() {
        def items1 = [1, 2, 3, 4, 5]
        withPool {
            items1.asConcurrent {
                items1.findAll {true}
            }
            def result = [].asSynchronized()
            items1.findAll {
                sleep 100; result << Thread.currentThread()
                true
            }
            assert result.unique().size() == 1
        }
    }

    public void testConcurrentEachOutsideOfTheWithPoolBlock() {
        def items1 = [1, 2, 3, 4, 5]
        withPool {
            items1.asConcurrent {
                def result = [].asSynchronized()
                items1.each {
                    sleep 100; result << Thread.currentThread()
                }
                assert result.unique().size() > 1
            }
        }

        def result = [].asSynchronized()
        items1.each {
            sleep 100; result << Thread.currentThread()
        }
        assert result.unique().size() == 1
    }
}
